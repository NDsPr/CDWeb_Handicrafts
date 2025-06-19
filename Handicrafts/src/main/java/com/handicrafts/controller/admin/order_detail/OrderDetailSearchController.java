package com.handicrafts.controller.admin.order_detail;

import com.handicrafts.util.BlankInputUtil;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${order.detail.search.view}")
    private String orderDetailSearchView;

    @Value("${order.detail.management.url}")
    private String orderDetailManagementUrl;

    @Value("${error.attribute}")
    private String errorAttribute;

    @Value("${error.blank}")
    private String errorBlankMessage;

    @Value("${error.numeric}")
    private String errorNumericMessage;

    @GetMapping
    public String showSearchPage() {
        return orderDetailSearchView;
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
            error = errorBlankMessage;
            isValid = false;
        } else if (!isNumeric(orderId)) {
            error = errorNumericMessage;
            isValid = false;
        }

        if (isValid) {
            return "redirect:" + orderDetailManagementUrl + "?orderId=" + orderId;
        } else {
            model.addAttribute(errorAttribute, error);
            return orderDetailSearchView;
        }
    }

    private static boolean isNumeric(String input) {
        // Sử dụng regex để kiểm tra xem chuỗi có phải chỉ là số hay không
        Pattern pattern = Pattern.compile("\\d*");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
