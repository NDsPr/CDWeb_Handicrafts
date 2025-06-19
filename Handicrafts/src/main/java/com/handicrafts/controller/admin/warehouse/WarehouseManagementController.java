package com.handicrafts.controller.admin.warehouse;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/warehouse/management")
public class WarehouseManagementController {

    private final Environment environment;

    public WarehouseManagementController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping
    public String showWarehouseManagementPage() {
        return environment.getProperty("warehouse.management.view", "admin/warehouse/management");
    }
}
