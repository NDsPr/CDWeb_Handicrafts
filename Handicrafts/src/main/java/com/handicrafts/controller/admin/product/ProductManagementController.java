package com.handicrafts.controller.admin.product;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product/management")
@RequiredArgsConstructor
public class ProductManagementController {

    private final Environment environment;

    @GetMapping
    public String showProductManagement() {
        return environment.getProperty("product.management.view", "admin/product/management");
    }
}
