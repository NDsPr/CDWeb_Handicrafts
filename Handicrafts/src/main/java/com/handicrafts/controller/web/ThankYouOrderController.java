package com.handicrafts.controller.web;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.CustomizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThankYouOrderController {

    private final CustomizeRepository customizeRepository;

    @Autowired
    public ThankYouOrderController(CustomizeRepository customizeRepository) {
        this.customizeRepository = customizeRepository;
    }

    @GetMapping("/thankyou")
    public String showThankYouPage(Model model) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        model.addAttribute("customizeInfo", customizeInfo);
        return "web/thankyou"; // Tên file thankyou.jsp hoặc thankyou.html nếu dùng Thymeleaf
    }
}
