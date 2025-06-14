package com.handicrafts.controller.admin.order_detail;

import com.ltw.bean.OrderDetailBean;
import com.ltw.dao.OrderDetailDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = {"/admin/order-detail-management"})
public class OrderDetailManagementController extends HttpServlet {
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

    // TODO: Thêm orderId lên đầu của trang
    // TODO: Thêm nút thông tin đơn hàng trong trang Đơn hàng cho mỗi một đơn hàng
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        List<OrderDetailBean> orderDetailList = orderDetailDAO.findDetailByOrderId(orderId);
        req.setAttribute("orderDetailList", orderDetailList);
        req.getRequestDispatcher("/order-detail-management.jsp").forward(req, resp);
    }
}
