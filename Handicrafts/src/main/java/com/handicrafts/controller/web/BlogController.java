package com.handicrafts.controller.web;

import com.ltw.bean.BlogBean;
import com.ltw.bean.CustomizeBean;
import com.ltw.dao.BlogDAO;
import com.ltw.dao.CustomizeDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@WebServlet(value = {"/blog"})
public class BlogController extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();
    private final BlogDAO BlogDAO = new BlogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<BlogBean> listBlogs = BlogDAO.findAllBlogs();
        CustomizeBean customizeInfo = customizeDAO.getCustomizeInfo();

        req.setAttribute("customizeInfo", customizeInfo);
        req.setAttribute("listBlogs", listBlogs);
        req.getRequestDispatcher("blog.jsp").forward(req, resp);
    }
}