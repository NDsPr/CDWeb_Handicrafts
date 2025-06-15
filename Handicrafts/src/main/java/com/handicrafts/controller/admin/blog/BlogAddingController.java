package com.handicrafts.controller.admin.blog;

import com.handicrafts.bean.BlogBean;
import com.handicrafts.dao.BlogDAO;
import com.handicrafts.util.BlankInputUtil;
import com.handicrafts.util.NumberValidateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet("/admin/blog-management/adding")

public class BlogAddingController extends HttpServlet {

    private final BlogDAO blogDAO = new BlogDAO();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/adding-blog.jsp").forward(req, resp);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String content = req.getParameter("content");
        String categoryId = req.getParameter("categoryId");
        String profilePic = req.getParameter("profilePic");
        String status = req.getParameter("status");

        String success = "success";
        String[] inputsForm = new String[] {title, description, content, categoryId,profilePic, status};
        ArrayList<String> errors = new ArrayList<>();
        // Biến bắt lỗi
        boolean isValid = true;

        for (String string : inputsForm) {
            if (BlankInputUtil.isBlank(string)) {
                errors.add("e");
                if (isValid) {
                    isValid = false;
                }
            } else {
                errors.add(null);
            }
        }
        req.setAttribute("errors", errors);

        // Nếu không lỗi thì lưu vào database
        if (isValid) {
            // Đổi String về số
            int statusInt = NumberValidateUtil.toInt(status);
            int categoryIdInt = NumberValidateUtil.toInt(categoryId);
            // Set thuộc tính vào bean
            BlogBean blogBean = new BlogBean();
            blogBean.setTitle(title);
            blogBean.setDescription(description);
            blogBean.setContent(content);
            blogBean.setCategoryId(categoryIdInt);
            blogBean.setProfilePic(profilePic);
            blogBean.setStatus(statusInt);


            blogDAO.createBlog(blogBean);
            resp.sendRedirect(req.getContextPath() + "/admin/blog-management/adding?success=" + success);
        } else {
            req.getRequestDispatcher("/adding-blog.jsp").forward(req, resp);
        }
    }
}


