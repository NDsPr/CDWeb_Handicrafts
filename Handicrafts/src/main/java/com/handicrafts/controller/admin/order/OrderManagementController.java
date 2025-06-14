package com.handicrafts.controller.admin.order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = {"/admin/order-management"})

public class OrderManagementController extends HttpServlet {
//    private final OrderDAO orderDAO = new OrderDAO();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<OrderBean> listOrders = orderDAO.findAllOrders();
//
//        request.setAttribute("listOrders", listOrders);
        request.getRequestDispatcher("/order-management.jsp").forward(request,response);
    }
}
