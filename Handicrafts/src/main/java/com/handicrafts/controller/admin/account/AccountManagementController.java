package com.handicrafts.controller.admin.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/account-management")
public class AccountManagementController {

    @Autowired
    private Environment environment;

    @GetMapping
    public String showAccountManagement() {
        return "account-management"; // Trả về tên view (không cần .jsp nếu bạn đã cấu hình ViewResolver)
    }
}
