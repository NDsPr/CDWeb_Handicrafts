package com.handicrafts.service;

import com.handicrafts.dto.ProductImageDTO;

import java.util.List;

public interface ImageService {
    List<ProductImageDTO> findImagesByProductId(int productId);
}
