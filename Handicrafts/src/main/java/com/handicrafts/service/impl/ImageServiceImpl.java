package com.handicrafts.service.impl;

import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.repository.ImageRepository;
import com.handicrafts.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public List<ProductImageDTO> findImagesByProductId(int productId) {
        return imageRepository.findImagesByProductId(productId);
    }

    @Override
    public List<ProductImageDTO> findAllImages() {
        return imageRepository.findAllImages();
    }

    @Override
    public ProductImageDTO findImageById(int id) {
        return imageRepository.findImageById(id);
    }

    @Override
    public boolean deleteImage(int id) {
        // Trước khi xóa ảnh từ database, lấy thông tin để xóa từ storage
        ProductImageDTO image = imageRepository.findImageById(id);
        if (image == null) {
            return false;
        }

        // Xóa ảnh từ storage nếu có nameInStorage
        if (image.getNameInStorage() != null && !image.getNameInStorage().isEmpty()) {
            deleteImageFromStorage(image.getNameInStorage());
        }

        // Sau đó xóa từ database
        return imageRepository.deleteImage(id);
    }

    @Override
    public boolean deleteImageFromStorage(String nameInStorage) {
        try {
            // Xóa file từ hệ thống file local
            File file = new File(uploadPath + File.separator + nameInStorage);
            if (file.exists()) {
                return file.delete();
            }
            return true; // Trả về true nếu file không tồn tại (coi như đã xóa)
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateImage(ProductImageDTO imageDTO) {
        // Kiểm tra xem ảnh có tồn tại không
        ProductImageDTO existingImage = imageRepository.findImageById(imageDTO.getId());
        if (existingImage == null) {
            return false;
        }

        // Nếu có cập nhật file ảnh mới và có ảnh cũ, xóa ảnh cũ từ storage
        if (imageDTO.getNameInStorage() != null && !imageDTO.getNameInStorage().isEmpty() &&
                existingImage.getNameInStorage() != null && !existingImage.getNameInStorage().isEmpty() &&
                !imageDTO.getNameInStorage().equals(existingImage.getNameInStorage())) {
            deleteImageFromStorage(existingImage.getNameInStorage());
        }

        // Cập nhật thông tin ảnh trong database
        return imageRepository.updateImage(imageDTO);
    }
}
