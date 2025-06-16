// Spring Boot version of OrderAddingController
package com.handicrafts.controller.admin.order;

import com.handicrafts.dto.OrderDTO;
import com.handicrafts.service.IOrderService;
import com.handicrafts.util.BlankInputUtil;
import com.handicrafts.util.NumberValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/order-management/adding")
public class OrderAddingController {

    @Autowired
    private IOrderService orderService;

    @GetMapping
    public String showForm(Model model) {
        return "admin/adding-order"; // view name (adding-order.jsp equivalent)
    }

    @PostMapping
    public String handleAddOrder(@RequestParam("userId") String userId,
                                 @RequestParam("total") String total,
                                 @RequestParam("paymentMethod") String paymentMethod,
                                 @RequestParam("status") String status,
                                 Model model) {

        List<String> errors = new ArrayList<>();
        boolean isValid = true;
        for (String input : new String[]{userId, total, paymentMethod, status}) {
            if (BlankInputUtil.isBlank(input)) {
                errors.add("e");
                isValid = false;
            } else {
                errors.add(null);
            }
        }
        model.addAttribute("errors", errors);

        if (isValid) {
            int userIdInt = NumberValidateUtil.toInt(userId);
            double totalVal = Double.parseDouble(total);
            int statusInt = NumberValidateUtil.toInt(status);

            OrderDTO order = new OrderDTO();
            order.setUserId(userIdInt);
            order.setTotal(totalVal);
            order.setPaymentMethod(paymentMethod);
            order.setStatus(statusInt);

            orderService.createOrder(order);

            return "redirect:/admin/order-management/adding?success=success";
        } else {
            return "admin/adding-order";
        }
    }
}