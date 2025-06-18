package com.handicrafts.api.client;

import com.handicrafts.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggest-key")
@RequiredArgsConstructor
public class SuggestionAPI {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<?> getSuggestKeys(@RequestParam(name = "key", required = false) String key) {
        if (key == null || key.trim().isEmpty()) {
            return ResponseEntity.ok(""); // Trường hợp không có key, trả về chuỗi rỗng
        }

        List<String> suggestKeys = productRepository.getSuggestTitle(key.trim());
        return ResponseEntity.ok(suggestKeys); // Spring Boot sẽ tự động chuyển thành JSON
    }
}
