package com.handicrafts.controller.admin.order_detail;

import com.handicrafts.util.BlankInputUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(value = {"/admin/order-detail-search"})
public class OrderDetailSearchController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/searching-order-management.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        String error = "";
        boolean isValid = true;
        // Kiểm tra lỗi bỏ trống orderId
        if (BlankInputUtil.isBlank(orderId)) {
            error = "Không được để trống";
            isValid = false;
        } else if (!isNumeric(orderId)) {
            error = "Phải là định dạng số!";
            isValid = false;
        }

        if (isValid) {
            resp.sendRedirect(req.getContextPath() + "/admin/order-detail-management?orderId=" + orderId);
        } else {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/searching-order-management.jsp").forward(req, resp);
        }
    }

    private static boolean isNumeric(String input) {
        // Sử dụng regex để kiểm tra xem chuỗi có phải chỉ là số hay không
        Pattern pattern = Pattern.compile("\\d*");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
