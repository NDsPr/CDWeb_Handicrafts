package com.handicrafts.controller.admin.home;

import com.ltw.dao.AdminHomeDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = {"/admin/home"})
public class HomeController extends HttpServlet {
    private final AdminHomeDAO adminHomeDAO = new AdminHomeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Integer> countUser = adminHomeDAO.countUser();
        List<Integer> countProduct =  adminHomeDAO.countProduct();
        List<Integer> countOrder = adminHomeDAO.countOrder();

        req.setAttribute("countProduct", countProduct);
        req.setAttribute("countUser", countUser);
        req.setAttribute("countOrder", countOrder);
        req.getRequestDispatcher("/admin-home.jsp").forward(req, resp);
    }
}

