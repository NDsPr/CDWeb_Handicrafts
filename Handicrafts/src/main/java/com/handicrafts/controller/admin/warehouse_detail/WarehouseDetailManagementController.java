package com.handicrafts.controller.admin.warehouse_detail;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/warehouse_detail/management")
public class WarehouseDetailManagementController {

    private final Environment environment;

    public WarehouseDetailManagementController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping
    public String showWarehouseDetailManagementPage(@RequestParam("warehouseId") int warehouseId, Model model) {
        model.addAttribute(
                environment.getProperty("model.attribute.warehouse.id", "warehouseId"),
                warehouseId
        );
        return environment.getProperty("warehouse.detail.management.view", "admin/warehouse/detail/management");
    }
}
