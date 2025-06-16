package com.handicrafts.controller.image;

import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AllImageManagementController {

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/admin/all-image-management")
    public String showAllImages(Model model) {
        List<ProductImageDTO> allImages = imageRepository.findAllImages();
        model.addAttribute("allImages", allImages);
        return "all-image-management"; // all-image-management.jsp trong thư mục templates (nếu dùng Thymeleaf)
    }
}
