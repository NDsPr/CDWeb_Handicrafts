package com.handicrafts.controller.web;

import com.ltw.bean.CustomizeBean;
import com.ltw.dao.CustomizeDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = {"/thankyou"})
public class ThankYouOrder extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomizeBean customizeInfo = customizeDAO.getCustomizeInfo();
        req.setAttribute("customizeInfo", customizeInfo);
        req.getRequestDispatcher("/thankyou.jsp").forward(req, resp);
    }
}
