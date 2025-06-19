package com.handicrafts.controller.admin.order_detail;

import com.handicrafts.util.BlankInputUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/admin/order-detail-search")
public class OrderDetailSearchController {

    private final Environment environment;

    @Autowired
    public OrderDetailSearchController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping
    public String showSearchPage() {
        return environment.getProperty("order.detail.search.view", "admin/order/search");
    }

    @PostMapping
    public String processSearch(
            @RequestParam(value = "orderId", required = false) String orderId,
            Model model,
            RedirectAttributes redirectAttributes) {

        String error = "";
        boolean isValid = true;

        // Kiểm tra lỗi bỏ trống orderId
        if (BlankInputUtil.isBlank(orderId)) {
            error = environment.getProperty("error.blank", "Vui lòng không để trống mã đơn hàng");
            isValid = false;
        } else if (!isNumeric(orderId)) {
            error = environment.getProperty("error.numeric", "Mã đơn hàng phải là số");
            isValid = false;
        }

        if (isValid) {
            return "redirect:" + environment.getProperty("order.detail.management.url", "/admin/order-detail-management") + "?orderId=" + orderId;
        } else {
            model.addAttribute(
                    environment.getProperty("error.attribute", "error"),
                    error
            );
            return environment.getProperty("order.detail.search.view", "admin/order/search");
        }
    }

    private static boolean isNumeric(String input) {
        // Sử dụng regex để kiểm tra xem chuỗi có phải chỉ là số hay không
        Pattern pattern = Pattern.compile("\\d*");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
