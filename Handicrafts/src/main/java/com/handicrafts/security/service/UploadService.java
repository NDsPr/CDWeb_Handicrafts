package com.handicrafts.security.service;

import com.ltw.bean.ProductImageBean;
import com.ltw.dao.ImageDAO;

import javax.servlet.http.Part;

public class UploadService {
    private final ImageDAO imageDAO = new ImageDAO();

    // Service kiểm tra xem file gửi lên có phải file ảnh hay không
    public boolean isImageFile(Part part) {
        return (part.getSubmittedFileName().endsWith(".jpg") || part.getSubmittedFileName().endsWith(".png"));
    }

    // Service upload file xuống Database
    public int insertProductImage(ProductImageBean image) {
        return imageDAO.insertProductImage(image);
    }

    // Service lấy lên ảnh theo id
    public ProductImageBean findImageById(int id) {
        return imageDAO.findImageById(id);
    }
}
