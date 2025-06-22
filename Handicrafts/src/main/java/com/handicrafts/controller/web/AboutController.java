package com.handicrafts.controller.web;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.CustomizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        // Dummy team member list
        List<Map<String, String>> teamMembers = new ArrayList<>();

        teamMembers.add(Map.of(
                "image", "/client/images/members/member1.png",
                "firstName", "Đào",
                "lastName", "Lê",
                "description", "Tôi cung cấp những sản phẩm về gốm sứ đã được chọn lọc kỹ càng..."
        ));

        teamMembers.add(Map.of(
                "image", "/client/images/members/member2.png",
                "firstName", "Duyên",
                "lastName", "Nguyễn",
                "description", "Tôi cung cấp sản phẩm đan lát gần gũi môi trường từ Đồng Tháp..."
        ));

        model.addAttribute("teamMembers", teamMembers);

        return "web/about-us"; // Tên file Thymeleaf
    }

}
