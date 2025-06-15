package com.handicrafts.controller.admin.blog;

import com.handicrafts.bean.BlogBean;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dao.BlogDAO;
import com.handicrafts.service.LogService;
import com.handicrafts.util.NumberValidateUtil;
import com.handicrafts.util.ValidateParamUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;


@WebServlet("/admin/blog-management/editing")

public class BlogEditingController extends HttpServlet {
    private final BlogDAO blogDAO = new BlogDAO();
    private LogService<BlogBean> logService = new LogService<>();
    private ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    // Todo: Cần chỉnh sửa lại logic cho thêm người dùng
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        BlogBean blogBean = blogDAO.findBlogById(id);
        request.setAttribute("blog", blogBean);
        request.getRequestDispatcher("/editing-blog.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // Sử dụng vòng lặp để set lỗi để trống theo index,
        // tuy nhiên cần phải giữ đúng thứ tự của input theo form và theo database (Vì sử dụng vòng lặp theo i để set lỗi)
        String idStr = req.getParameter("id");
        // Lấy id kiểu int ra để lưu vào database
        int id = Integer.parseInt(idStr);
        String title =req.getParameter("title");
        String author = req.getParameter("author");
        String description = req.getParameter("description");
        String content = req.getParameter("content");
        String categoryId= req.getParameter("categoryId");
        String status = req.getParameter("status");
        String profilePic=req.getParameter("profilePic");
        String createdDate = req.getParameter("createdDate");
        String createdBy = req.getParameter("createdBy");
        String modifiedDate = req.getParameter("modifiedDate");
        String modifiedBy = req.getParameter("modifiedBy");

        // Biến thông báo trạng thái
        String msg = "";

        String[] inputsForm = {title, author, description, content, categoryId, status, profilePic, createdDate,createdBy,modifiedDate,modifiedBy};
        // Biến bắt lỗi
        boolean isValid = true;

        // Kiểm tra input rằng/null trong hàm checkEmptyParam
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        // Nếu có lỗi (khác null) trả về isValid = false
        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        BlogBean prevBlog = blogDAO.findBlogById(id);
        if (isValid) {
            // Đọc lỗi với database
            int categoryIdInt = NumberValidateUtil.toInt(categoryId);
            int statusInt = NumberValidateUtil.toInt(status);

            // Set thuộc tính vào bean
            BlogBean nowChange = new BlogBean();
            nowChange.setId(id);
            nowChange.setTitle(title);
            nowChange.setAuthor(author);
            nowChange.setDescription(description);
            nowChange.setContent(content);
            nowChange.setCategoryId(categoryIdInt);
            nowChange.setStatus(statusInt);
            nowChange.setProfilePic(profilePic);
            nowChange.setCreatedBy(createdBy);
            nowChange.setModifiedBy(modifiedBy);
            int affectedRow = blogDAO.updateBlog(nowChange);
            // Sau khi update, cập nhật nội dung hiện tại trong db
            BlogBean currentBlog = blogDAO.findBlogById(id);

            if (affectedRow < 0) {
                logService.log(req, "admin-update-account", LogState.FAIL, LogLevel.ALERT, prevBlog, currentBlog);
                msg = "error";
            } else if (affectedRow > 0) {
                logService.log(req, "admin-update-account", LogState.SUCCESS, LogLevel.WARNING, prevBlog, currentBlog);
                msg = "success";
            } else {
                // Lỗi nhập liệu người dùng thì không ghi log
                msg = "error";
            }
        } else {
            req.setAttribute("errors", errors);
            BlogBean currentBlog = blogDAO.findBlogById(id);
            logService.log(req, "admin-update-blog", LogState.FAIL, LogLevel.ALERT, prevBlog, currentBlog);
            msg = "error";
        }

        BlogBean displayBlog = blogDAO.findBlogById(id);
        req.setAttribute("msg", msg);
        req.setAttribute("displayUser", displayBlog);
        req.getRequestDispatcher("/editing-blog.jsp").forward(req, resp);
    }
}