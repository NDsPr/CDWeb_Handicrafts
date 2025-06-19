package com.handicrafts.security.service;

import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.repository.ImageRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.Part;

@Service
public class UploadService {

    private final ImageRepository imageRepository;

    public UploadService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public boolean isImageFile(Part part) {
        String fileName = part.getSubmittedFileName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".png");
    }

    public int insertProductImage(ProductImageDTO image) {
        return imageRepository.insertProductImage(image);
    }

    public ProductImageDTO findImageById(int id) {
        return imageRepository.findImageById(id);
    }
}
