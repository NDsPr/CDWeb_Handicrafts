package com.handicrafts.controller.admin.warehouse_detail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("${warehouse.detail.management.url}")
public class WarehouseDetailManagementController {

    @Value("${warehouse.detail.management.view}")
    private String warehouseDetailView;

    @Value("${model.attribute.warehouse.id}")
    private String warehouseIdAttribute;

    @GetMapping
    public String showWarehouseDetailManagementPage(@RequestParam("warehouseId") int warehouseId, Model model) {
        model.addAttribute(warehouseIdAttribute, warehouseId);
        return warehouseDetailView;
    }
}
