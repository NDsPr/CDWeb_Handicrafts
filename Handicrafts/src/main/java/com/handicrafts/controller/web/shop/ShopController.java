package com.handicrafts.controller.web.shop;

import com.handicrafts.dto.*;
import com.handicrafts.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShopController {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

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
        try {
            logger.info("Loading shop page");

            // Lấy thông tin customize
            CustomizeDTO customizeInfo = null;
            try {
                customizeInfo = customizeRepository.getCustomizeInfo();
            } catch (Exception e) {
                logger.error("Error fetching customize info: {}", e.getMessage());
            }

            if (customizeInfo == null) {
                logger.warn("CustomizeInfo is null, creating empty object");
                customizeInfo = new CustomizeDTO(); // Tạo đối tượng rỗng để tránh NullPointerException
            }

            // Lấy danh sách categories
            List<CategoryDTO> categories = Collections.emptyList(); // Khởi tạo với empty list
            try {
                List<CategoryDTO> fetchedCategories = categoryRepository.findAllCategories();
                if (fetchedCategories != null && !fetchedCategories.isEmpty()) {
                    categories = fetchedCategories;
                }
                logger.info("Categories loaded: {}", categories.size());
            } catch (Exception e) {
                logger.error("Error fetching categories: {}", e.getMessage());
            }

            // Khởi tạo các map
            Map<Integer, List<CategoryTypeDTO>> categoryTypeMap = new HashMap<>();
            Map<Integer, List<ProductDTO>> productMap = new HashMap<>();
            Map<Integer, ProductImageDTO> imageMap = new HashMap<>();

            // Xử lý dữ liệu cho từng category
            for (CategoryDTO category : categories) {
                try {
                    if (category == null) {
                        logger.warn("Skipping null category");
                        continue;
                    }

                    int categoryId = category.getId();
                    logger.debug("Processing category ID: {}", categoryId);

                    // Sản phẩm theo category
                    List<ProductDTO> products = Collections.emptyList();
                    try {
                        List<ProductDTO> fetchedProducts = productRepository.findThreeProductByCategoryId(categoryId);
                        if (fetchedProducts != null && !fetchedProducts.isEmpty()) {
                            products = fetchedProducts;
                            logger.info("Found {} products for category ID: {}", products.size(), categoryId);
                        } else {
                            logger.warn("No products found for category ID: {}", categoryId);
                        }
                    } catch (Exception e) {
                        logger.error("Error fetching products for category {}: {}", categoryId, e.getMessage());
                    }

                    productMap.put(categoryId, products);

                    // Ảnh theo product - Cải thiện phần này
                    for (ProductDTO product : products) {
                        if (product != null) {
                            try {
                                ProductImageDTO image = imageRepository.findOneByProductId(product.getId());
                                if (image != null) {
                                    logger.info("Found image for product ID {}: {}", product.getId(), image.getLink());
                                    imageMap.put(product.getId(), image);
                                } else {
                                    logger.warn("No image found for product ID: {}", product.getId());
                                }
                            } catch (Exception e) {
                                logger.error("Error fetching image for product {}: {}", product.getId(), e.getMessage());
                            }
                        }
                    }

                    // Loại sản phẩm theo category
                    List<CategoryTypeDTO> categoryTypes = Collections.emptyList();
                    try {
                        List<CategoryTypeDTO> fetchedCategoryTypes = categoryTypeRepository.findCategoryTypeByCategoryId(categoryId);
                        if (fetchedCategoryTypes != null && !fetchedCategoryTypes.isEmpty()) {
                            categoryTypes = fetchedCategoryTypes;
                        }
                    } catch (Exception e) {
                        logger.error("Error fetching category types for category {}: {}", categoryId, e.getMessage());
                    }

                    categoryTypeMap.put(categoryId, categoryTypes);
                } catch (Exception e) {
                    logger.error("Error processing category: {}", e.getMessage());
                    // Tiếp tục với category tiếp theo
                }
            }

            // Debug thông tin về imageMap
            logger.info("ImageMap size: {}", imageMap.size());
            for (Map.Entry<Integer, ProductImageDTO> entry : imageMap.entrySet()) {
                if (entry.getValue() != null) {
                    logger.info("ImageMap entry - Product ID: {}, Image link: {}",
                            entry.getKey(), entry.getValue().getLink());
                } else {
                    logger.warn("ImageMap entry - Product ID: {} has null image", entry.getKey());
                }
            }

            // Thêm dữ liệu vào model
            model.addAttribute("customizeInfo", customizeInfo);
            model.addAttribute("categories", categories);
            model.addAttribute("categoryTypeMap", categoryTypeMap);
            model.addAttribute("productMap", productMap);
            model.addAttribute("imageMap", imageMap);
            model.addAttribute("categoriesForProduct", categories);

            // Thêm thông tin debug vào model
            model.addAttribute("imageMapSize", imageMap.size());

            logger.info("Shop page model prepared successfully");
            return "web/shop";
        } catch (Exception e) {
            logger.error("Unhandled exception in showShopPage: ", e);
            if (e.getCause() != null) {
                logger.error("Caused by: {}", e.getCause().getMessage());
            }
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi tải trang. Vui lòng thử lại sau.");
            model.addAttribute("errorDetails", e.getMessage());
            return "error/general";
        }
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(Exception ex) {
        logger.error("Exception during execution of ShopController: ", ex);
        if (ex.getCause() != null) {
            logger.error("Caused by: {}", ex.getCause().getMessage());
        }
        ModelAndView modelAndView = new ModelAndView("error/general");
        modelAndView.addObject("errorMessage", "Có lỗi xảy ra khi tải trang. Vui lòng thử lại sau.");
        modelAndView.addObject("errorDetails", ex.getMessage());
        if (ex.getCause() != null) {
            modelAndView.addObject("errorCause", ex.getCause().getMessage());
        }
        return modelAndView;
    }
    // Trong ShopController
    private Map<Integer, ProductImageDTO> prepareImageMap(List<ProductDTO> allProducts) {
        Map<Integer, ProductImageDTO> imageMap = new HashMap<>();

        for (ProductDTO product : allProducts) {
            try {
                ProductImageDTO image = imageRepository.findOneByProductId(product.getId());
                if (image != null) {
                    logger.info("Found image for product ID {}: {}", product.getId(), image.getLink());
                    imageMap.put(product.getId(), image);
                } else {
                    logger.warn("No image found for product ID: {}", product.getId());
                    // Tạo hình ảnh mặc định
                    ProductImageDTO defaultImage = new ProductImageDTO();
                    defaultImage.setId(-1);
                    defaultImage.setProductId(product.getId());
                    defaultImage.setLink("/client/images/no-image.png");
                    imageMap.put(product.getId(), defaultImage);
                }
            } catch (Exception e) {
                logger.error("Error fetching image for product {}: {}", product.getId(), e.getMessage());
                // Thêm hình ảnh mặc định trong trường hợp lỗi
                ProductImageDTO defaultImage = new ProductImageDTO();
                defaultImage.setId(-1);
                defaultImage.setProductId(product.getId());
                defaultImage.setLink("/client/images/no-image.png");
                imageMap.put(product.getId(), defaultImage);
            }
        }

        logger.info("ImageMap size: {}", imageMap.size());
        return imageMap;
    }

}
