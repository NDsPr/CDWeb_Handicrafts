package com.handicrafts.controller.web;

import com.ltw.bean.CustomizeBean;
import com.ltw.dao.CustomizeDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/order-history")
public class OrderHistoryController extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomizeBean customizeInfo = customizeDAO.getCustomizeInfo();
        request.setAttribute("customizeInfo", customizeInfo);
        request.getRequestDispatcher("/order-history.jsp").forward(request, response);
    }
}
