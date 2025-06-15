package com.handicrafts.controller.web.shop;

import com.handicrafts.dto.*;
import com.handicrafts.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShopDetailByTypeController {

    private final CustomizeRepository customizeRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public ShopDetailByTypeController(CustomizeRepository customizeRepository,
                                      CategoryRepository categoryRepository,
                                      CategoryTypeRepository categoryTypeRepository,
                                      ProductRepository productRepository,
                                      ImageRepository imageRepository) {
        this.customizeRepository = customizeRepository;
        this.categoryRepository = categoryRepository;
        this.categoryTypeRepository = categoryTypeRepository;
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    @GetMapping("/shop-detail-by-type")
    public String showShopDetailByType(@RequestParam("categoryTypeId") int categoryTypeId,
                                       @RequestParam("recentPage") int recentPage,
                                       @RequestParam("sort") String sort,
                                       @RequestParam("range") String range,
                                       Model model) {

        int totalPages = getTotalPagesByCategoryType(categoryTypeId);
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();

        List<CategoryDTO> categories = categoryRepository.findAllCategories();
        Map<Integer, List<CategoryTypeDTO>> categoryTypeMap = new HashMap<>();

        CategoryTypeDTO categoryType = categoryTypeRepository.findTypeById(categoryTypeId);
        double[] rangeLimit = getLimitRange(range);

        List<ProductDTO> products = productRepository.findByTypeIdAndLimit(categoryTypeId, rangeLimit, sort, getStartLimit(recentPage), 2);

        Map<Integer, ProductImageDTO> imageMap = new HashMap<>();
        for (ProductDTO product : products) {
            ProductImageDTO thumbnailImage = imageRepository.findOneByProductId(product.getId());
            imageMap.put(product.getId(), thumbnailImage);
        }

        for (CategoryDTO category : categories) {
            List<CategoryTypeDTO> categoryTypes = categoryTypeRepository.findCategoryTypeByCategoryId(category.getId());
            categoryTypeMap.put(category.getId(), categoryTypes);
        }

        model.addAttribute("customizeInfo", customizeInfo);
        model.addAttribute("serverPage", recentPage);
        model.addAttribute("serverTotalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("range", range);
        model.addAttribute("categoryType", categoryType);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryTypeMap", categoryTypeMap);
        model.addAttribute("imageMap", imageMap);

        return "shop-detail-type"; // JSP hoặc Thymeleaf
    }

    private double[] getLimitRange(String range) {
        if (!"none".equals(range)) {
            switch (range) {
                case "0-to-499": return new double[]{0, 499000.0};
                case "500-to-2999": return new double[]{500000.0, 2999000.0};
                case "3000-to-9999": return new double[]{3000000.0, 9999000.0};
                case "up-to-10000": return new double[]{10000000.0, 10000000000.0};
            }
        }
        return null;
    }

    private int getTotalPagesByCategoryType(int categoryTypeId) {
        int totalItems = productRepository.getTotalItemsByCategoryType(categoryTypeId);
        return (int) Math.ceil((double) totalItems / 2);
    }

    private int getStartLimit(int page) {
        return 2 * (page - 1);
    }
}
