package com.handicrafts.service;

import com.handicrafts.dto.ProductImageDTO;

import java.util.List;

public interface ImageService {
    List<ProductImageDTO> findImagesByProductId(int productId);
    List<ProductImageDTO> findAllImages();

    ProductImageDTO findImageById(int id);
    boolean deleteImage(int id);
    boolean updateImage(ProductImageDTO imageDTO);

    // Phương thức này có thể được triển khai theo cách khác không phụ thuộc vào CloudStorageService
    boolean deleteImageFromStorage(String imagePath);
}
