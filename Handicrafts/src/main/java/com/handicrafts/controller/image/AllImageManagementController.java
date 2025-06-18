package com.handicrafts.controller;

import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.service.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AllImageManagementController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/all-image-management")
    public String showAllImages(Model model) {
        List<ProductImageDTO> images = imageService.findAllImages();
        model.addAttribute("images", images);
        return "all-image-management";
    }
}
