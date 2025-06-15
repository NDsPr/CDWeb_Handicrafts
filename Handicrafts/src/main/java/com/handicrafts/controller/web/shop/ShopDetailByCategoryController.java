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
public class ShopDetailByCategoryController {

    private final CustomizeRepository customizeRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public ShopDetailByCategoryController(CustomizeRepository customizeRepository,
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

    @GetMapping("/shop-detail-by-category")
    public String showShopDetailByCategory(@RequestParam("categoryId") int categoryId, Model model) {
        // Lấy thông tin cấu hình website
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();

        // Navigation trái - danh sách danh mục
        List<CategoryDTO> categories = categoryRepository.findAllCategories();
        Map<Integer, List<CategoryTypeDTO>> categoryTypeMap = new HashMap<>();

        for (CategoryDTO category : categories) {
            List<CategoryTypeDTO> categoryTypes = categoryTypeRepository.findCategoryTypeByCategoryId(category.getId());
            categoryTypeMap.put(category.getId(), categoryTypes);
        }

        // Lấy loại sản phẩm thuộc category đang chọn
        List<CategoryTypeDTO> categoryTypeForProduct = categoryTypeRepository.findCategoryTypeByCategoryId(categoryId);

        // Hiển thị sản phẩm theo loại
        Map<Integer, List<ProductDTO>> productMap = new HashMap<>();
        Map<Integer, ProductImageDTO> imageMap = new HashMap<>();

        for (CategoryTypeDTO categoryType : categoryTypeForProduct) {
            List<ProductDTO> products = productRepository.findFourProductByTypeId(categoryType.getId());
            for (ProductDTO product : products) {
                imageMap.put(product.getId(), imageRepository.findOneByProductId(product.getId()));
            }
            productMap.put(categoryType.getId(), products);
        }

        model.addAttribute("customizeInfo", customizeInfo);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryTypeMap", categoryTypeMap);
        model.addAttribute("categoryTypeForProduct", categoryTypeForProduct);
        model.addAttribute("productMap", productMap);
        model.addAttribute("imageMap", imageMap);

        return "shop-detail"; // trả về file `shop-detail.jsp` hoặc `shop-detail.html`
    }
}
