package com.handicrafts.controller.admin.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${product.management.base.url}")
public class ProductManagementController {

    @Value("${product.management.view}")
    private String productManagementView;

    @GetMapping
    public String showProductManagement() {
        return productManagementView;
    }
}
