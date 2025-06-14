package com.handicrafts.controller.web;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.CustomizeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    private final CustomizeRepository customizeRepository = new CustomizeRepository();

    @GetMapping("/about-us")
    public String aboutPage(Model model) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        model.addAttribute("customizeInfo", customizeInfo);
        return "about"; // Tên file Thymeleaf: about.html
    }
}
