package com.handicrafts.controller.admin.warehouse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/warehouse-management")
public class WarehouseManagementController {

    @GetMapping
    public String showWarehouseManagementPage() {
        // Trả về tên view (VD: warehouse-management.html hoặc warehouse-management.jsp)
        return "warehouse-management";
    }
}
