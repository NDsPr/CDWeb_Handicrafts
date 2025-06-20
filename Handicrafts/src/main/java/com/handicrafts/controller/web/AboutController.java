package com.handicrafts.controller.web;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.CustomizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    private final CustomizeRepository customizeRepository;

    @Autowired
    public AboutController(CustomizeRepository customizeRepository) {
        this.customizeRepository = customizeRepository;
    }

    @GetMapping("/about-us")
    public String aboutPage(Model model) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        model.addAttribute("customizeInfo", customizeInfo);
        return "web/about"; // Tên file Thymeleaf: about.html
    }
}
