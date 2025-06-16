package com.handicrafts.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handicrafts.dto.ProductDTO;
import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.repository.ImageRepository;
import com.handicrafts.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductPsrAPI {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/api/product-psr")
    public ResponseEntity<String> getProductsByCategory(
            @RequestParam("categoryTypeId") int categoryTypeId,
            @RequestParam("recentPage") int recentPage,
            @RequestParam("sort") String sort,
            @RequestParam("range") String range
    ) throws JsonProcessingException {
        int totalPages = getTotalPagesByCategoryType(categoryTypeId);
        double[] rangeLimit = getLimitRange(range);

        List<ProductDTO> products = productRepository.findByTypeIdAndLimit(
                categoryTypeId,
                rangeLimit,
                sort,
                getStartLimit(recentPage),
                2 // fixed page size
        );

        for (ProductDTO product : products) {
            List<ProductImageDTO> thumbnailImage = new ArrayList<>();
            thumbnailImage.add(imageRepository.findOneByProductId(product.getId()));
            product.setImages(thumbnailImage);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("recentPage", recentPage);
        response.put("totalPages", totalPages);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToClient = objectMapper.writeValueAsString(response);

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(jsonToClient);
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

    private int getTotalPagesByCategoryType(int categoryTypeId) {
        int totalItems = productRepository.getTotalItemsByCategoryType(categoryTypeId);
        return (int) Math.ceil((double) totalItems / 2);
    }

    private int getStartLimit(int page) {
        return 2 * (page - 1);
    }
}