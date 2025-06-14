package com.handicrafts.controller.admin.blog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/blog-management")

public class BlogManagementController extends HttpServlet {

//    private final BlogDAO blogDAO = new BlogDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<BlogBean> blogs = blogDAO.findAllBlogs();
//        request.setAttribute("blogs", blogs);
        request.getRequestDispatcher("/blog-management.jsp").forward(request, response);
    }
}
