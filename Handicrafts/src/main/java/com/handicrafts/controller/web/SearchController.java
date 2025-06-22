package com.handicrafts.controller.web;

import com.handicrafts.dto.CategoryDTO;
import com.handicrafts.dto.CategoryTypeDTO;
import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.dto.ProductDTO;
import com.handicrafts.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private CustomizeRepository customizeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    private static final int PAGE_SIZE = 8;

    @GetMapping
    public String search(@RequestParam(name = "key", required = false) String key,
                         @RequestParam(name = "recentPage", defaultValue = "1") int page,
                         @RequestParam(name = "sort", defaultValue = "none") String sort,
                         @RequestParam(name = "range", defaultValue = "none") String range,
                         Model model) {

        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();

        List<CategoryDTO> categories = categoryRepository.findAllCategories();
        Map<Integer, List<CategoryTypeDTO>> categoryTypeMap = new HashMap<>();

        double[] rangeLimit = getLimitRange(range);
        int offset = getStartLimit(page);

        List<ProductDTO> products = productRepository.findByKeyAndLimit(key, rangeLimit, sort, offset, PAGE_SIZE);
        int totalItems = productRepository.countByKeyAndLimit(key, rangeLimit);
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);

        for (CategoryDTO category : categories) {
            List<CategoryTypeDTO> types = categoryTypeRepository.findCategoryTypeByCategoryId(category.getId());
            categoryTypeMap.put(category.getId(), types);
        }

        model.addAttribute("customizeInfo", customizeInfo);
        model.addAttribute("serverPage", page);
        model.addAttribute("serverTotalPages", totalPages);
        model.addAttribute("key", key);
        model.addAttribute("sort", sort);
        model.addAttribute("range", range);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryTypeMap", categoryTypeMap);

        return "web/search";
    }

    private double[] getLimitRange(String range) {
        if (!"none".equals(range)) {
            double[] rangeLimit = new double[2];
            switch (range) {
                case "0-to-499":
                    rangeLimit[0] = 0;
                    rangeLimit[1] = 499000.0;
                    break;
                case "500-to-2999":
                    rangeLimit[0] = 500000.0;
                    rangeLimit[1] = 2999000.0;
                    break;
                case "3000-to-9999":
                    rangeLimit[0] = 3000000.0;
                    rangeLimit[1] = 9999000.0;
                    break;
                case "up-to-10000":
                    rangeLimit[0] = 10000000.0;
                    rangeLimit[1] = 10000000000.0;
                    break;
            }
            return rangeLimit;
        } else {
            return null;
        }
    }

    private int getStartLimit(int page) {
        return PAGE_SIZE * (page - 1);
    }
}
