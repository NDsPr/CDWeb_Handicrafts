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

@WebServlet(value = {"/blog-detail"})
public class BlogDetailController extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();
    private final BlogDAO blogDetailDAO = new BlogDAO();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int blogId = Integer.parseInt(request.getParameter("id"));
        CustomizeBean customizeInfo = customizeDAO.getCustomizeInfo();
        BlogBean blogDetailBean = blogDetailDAO.findBlogById(blogId);

        request.setAttribute("customizeInfo", customizeInfo);
        request.setAttribute("blogDetail", blogDetailBean);
        request.getRequestDispatcher("blog-detail.jsp").forward(request, response);


    }
}

