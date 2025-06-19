package com.handicrafts.controller.image;

import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.service.ImageService;
import com.handicrafts.util.CloudStorageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AllImageDeleteController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/all-image-delete")
    public String deleteImage(@RequestParam("id") int id, Model model) throws IOException {
        ProductImageDTO image = imageService.findImageById(id);

        if (image != null) {
            // Xóa ảnh trên cloud storage
            CloudStorageUtil.delete(image.getNameInStorage());

            // Xóa ảnh trong database
            imageService.deleteImage(id);

            model.addAttribute("success", "s");
        } else {
            model.addAttribute("error", "e");
        }

        // Redirect để tránh việc refresh trang dẫn đến xóa lại
        return "redirect:/admin/all-image-management";
    }
}
