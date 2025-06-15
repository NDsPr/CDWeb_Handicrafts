package com.handicrafts.controller.admin.order;


import com.handicrafts.bean.OrderBean;
import com.handicrafts.dao.OrderDAO;
import com.handicrafts.util.BlankInputUtil;
import com.handicrafts.util.NumberValidateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/admin/order-management/adding")

public class OrderAddingController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/adding-order.jsp").forward(req, resp);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String userId = req.getParameter("userId");
        String total = req.getParameter("total");
        String paymentMethod = req.getParameter("paymentMethod");
        String status = req.getParameter("status");

        String success = "success";
        String[] inputsForm = new String[] {userId, total, paymentMethod, status};
        ArrayList<String> errors = new ArrayList<>();
        // Biến bắt lỗi
        boolean isValid = true;

        for (String string : inputsForm) {
            if (BlankInputUtil.isBlank(string)) {
                errors.add("e");
                if (isValid) {
                    isValid = false;
                }
            } else {
                errors.add(null);
            }
        }
        req.setAttribute("errors", errors);

        // Nếu không lỗi thì lưu vào database
        if (isValid) {
            // Đổi String về số
            int statusInt = NumberValidateUtil.toInt(status);
            int userIdInt = NumberValidateUtil.toInt(userId);
            // Set thuộc tính vào bean
            OrderBean orderBean = new OrderBean();
            orderBean.setUserId(userIdInt);
            orderBean.setTotal(Double.parseDouble(total));
            orderBean.setPaymentMethod(paymentMethod);
            orderBean.setStatus(statusInt);



            orderDAO.createOrder(orderBean);
            resp.sendRedirect(req.getContextPath() + "/admin/order-management/adding?success=" + success);
        } else {
            req.getRequestDispatcher("/adding-order.jsp").forward(req, resp);
        }
    }
}


