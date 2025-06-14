package com.handicrafts.controller.admin.warehouse_detail;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = {"/admin/warehouse-detail-management"})
public class WarehouseDetailManagementController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int warehouseId = Integer.parseInt(req.getParameter("warehouseId"));
        req.setAttribute("warehouseId", warehouseId);
        req.getRequestDispatcher("/warehouse-detail-management.jsp").forward(req, resp);
    }
}
