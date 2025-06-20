package com.handicrafts.controller.web;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.dto.ProductDTO;
import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.dto.ReviewDTO;
import com.handicrafts.repository.CustomizeRepository;
import com.handicrafts.repository.ImageRepository;
import com.handicrafts.repository.ProductRepository;
import com.handicrafts.repository.ReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductDetailController {

    private final CustomizeRepository customizeRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final ReviewRepository reviewRepository;

    public ProductDetailController(CustomizeRepository customizeRepository,
                                   ProductRepository productRepository,
                                   ImageRepository imageRepository,
                                   ReviewRepository reviewRepository) {
        this.customizeRepository = customizeRepository;
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/product-detail")
    public String showProductDetail(@RequestParam("id") int productId, Model model) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();

        ProductDTO productDetail = productRepository.findProductById(productId);
        List<ProductImageDTO> productImages = imageRepository.findImagesByProductId(productId);
        productDetail.setImages(productImages);

        List<ProductDTO> productSuggest = productRepository.findSixProductsForSuggest(
                productId, productDetail.getCategoryTypeId(), 0);

        for (ProductDTO product : productSuggest) {
            List<ProductImageDTO> thumbnail = imageRepository.getThumbnailByProductId(product.getId());
            product.setImages(thumbnail);
        }

        List<ReviewDTO> reviews = reviewRepository.findReviewPaginationByProductId(productId, 0, 0);

        model.addAttribute("customizeInfo", customizeInfo);
        model.addAttribute("productDetail", productDetail);
        model.addAttribute("productSuggest", productSuggest);
        model.addAttribute("reviews", reviews);

        return "web/product-detail"; // JSP or Thymeleaf view
    }
}
