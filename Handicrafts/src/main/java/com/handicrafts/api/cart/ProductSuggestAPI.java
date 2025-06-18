package com.handicrafts.api.cart;

import com.handicrafts.dto.ProductDTO;
import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.repository.ImageRepository;
import com.handicrafts.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-suggest")
@RequiredArgsConstructor
public class ProductSuggestAPI {

    private final IProductService productService;
    private final ImageRepository imageRepository;

    @GetMapping
    public ResponseEntity<?> suggestProducts(
            @RequestParam int currentPos,
            @RequestParam int categoryTypeId,
            @RequestParam int productId
    ) {
        List<ProductDTO> suggestedProducts = productService.findSixProductsForSuggest(productId, categoryTypeId, currentPos);

        for (ProductDTO product : suggestedProducts) {
            List<ProductImageDTO> thumbnails = imageRepository.findImagesByProductId(product.getId());
            product.setImages(thumbnails);
        }

        currentPos += 6;

        return ResponseEntity.ok().body(
                new SuggestResponse(suggestedProducts, currentPos)
        );
    }

    // Inner class cho phản hồi JSON
    private record SuggestResponse(List<ProductDTO> productList, int currentPos) {}
}
