package com.handicrafts.controller.web;

import com.ltw.bean.CustomizeBean;
import com.ltw.dao.CustomizeDAO;
import com.ltw.dao.OrderDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = {"/order-detail-history"})

public class OrderDetailHistoryController extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomizeBean customizeInfo = customizeDAO.getCustomizeInfo();
        request.setAttribute("customizeInfo", customizeInfo);
        // Lấy orderId từ URL
        int orderId = Integer.parseInt(request.getParameter("orderId"));

        // Chuyển hướng đến trang JSP hiển thị chi tiết đơn hàng
        request.setAttribute("orderId", orderId);
        request.getRequestDispatcher("/order-detail-history.jsp").forward(request, response);
    }
}
