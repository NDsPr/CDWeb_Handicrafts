package com.handicrafts.controller.web.shop;

import com.handicrafts.dto.*;
import com.handicrafts.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShopController {

    private final CustomizeRepository customizeRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public ShopController(CustomizeRepository customizeRepository,
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

    @GetMapping("/shop")
    public String showShopPage(Model model) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        List<CategoryDTO> categories = categoryRepository.findAllCategories();

        Map<Integer, List<CategoryTypeDTO>> categoryTypeMap = new HashMap<>();
        Map<Integer, List<ProductDTO>> productMap = new HashMap<>();
        Map<Integer, ProductImageDTO> imageMap = new HashMap<>();

        for (CategoryDTO category : categories) {
            int categoryId = category.getId();

            // Sản phẩm theo category
            List<ProductDTO> products = productRepository.findThreeProductByCategoryId(categoryId);
            productMap.put(categoryId, products);

            // Ảnh theo product
            for (ProductDTO product : products) {
                imageMap.put(product.getId(), imageRepository.findOneByProductId(product.getId()));
            }

            // Loại sản phẩm theo category
            List<CategoryTypeDTO> categoryTypes = categoryTypeRepository.findCategoryTypeByCategoryId(categoryId);
            categoryTypeMap.put(categoryId, categoryTypes);
        }

        model.addAttribute("customizeInfo", customizeInfo);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryTypeMap", categoryTypeMap);
        model.addAttribute("productMap", productMap);
        model.addAttribute("imageMap", imageMap);

        return "shop"; // shop.jsp (JSP) hoặc shop.html (Thymeleaf)
    }
}
