package com.handicrafts.controller.admin.warehouse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${warehouse.management.base.url}")
public class WarehouseManagementController {

    @Value("${warehouse.management.view}")
    private String warehouseManagementView;

    @GetMapping
    public String showWarehouseManagementPage() {
        return warehouseManagementView;
    }
}
