package com.handicrafts.service.impl;

import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.repository.ImageRepository;
import com.handicrafts.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<ProductImageDTO> findImagesByProductId(int productId) {
        return imageRepository.findImagesByProductId(productId);
    }
}
