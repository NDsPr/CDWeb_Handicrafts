package com.handicrafts.api.admin;

import com.handicrafts.bean.BlogBean;
import com.handicrafts.bean.UserBean;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dao.BlogDAO;
import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.service.LogService;
import com.handicrafts.util.SendEmailUtil;
import com.handicrafts.util.SessionUtil;
import com.handicrafts.util.TransferDataUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = { "/api/admin/blog"})
public class BlogAPI extends HttpServlet {
    private final BlogDAO blogDAO = new BlogDAO();
    private BlogBean prevBlog;
    private LogService<BlogBean> logService = new LogService<>();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
// Lấy ra các Property mà DataTable gửi về
        // Thông tin về phân trang
        int draw = Integer.parseInt(req.getParameter("draw"));      // Số thứ tự của request hiện tại
        int start = Integer.parseInt(req.getParameter("start"));    // Vị trí bắt đầu của dữ liệu
        int length = Integer.parseInt(req.getParameter("length"));  // Số phần tử trên một trang

        // Thông tin về tìm kiếm
        String searchValue = req.getParameter("search[value]");

        // Thông tin về sắp xếp
        String orderBy = req.getParameter("order[0][column]") == null ? "0" : req.getParameter("order[0][column]");
        String orderDir = req.getParameter("order[0][dir]") == null ? "asc" : req.getParameter("order[0][dir]");
        String columnOrder = req.getParameter("columns[" + orderBy + "][data]");      // Tên của cột muốn sắp xếp

        List<BlogBean> blogs = blogDAO.getBlogsDatatable(start, length, columnOrder, orderDir, searchValue);
        int recordsTotal = blogDAO.getRecordsTotal();
        // Tổng số record khi filter search
        int recordsFiltered = blogDAO.getRecordsFiltered(searchValue);
        draw++;

        DatatableDTO<BlogBean> blogDatatableDTO = new DatatableDTO<>(blogs, recordsTotal, recordsFiltered, draw);
        String jsonData = new TransferDataUtil<DatatableDTO<BlogBean>>().toJson(blogDatatableDTO);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String status;
        String notify;

        prevBlog = blogDAO.findBlogById(id);
        int affectedRow = blogDAO.deleteBlog(id);

        if (affectedRow < 1) {
            BlogBean currentBlog = blogDAO.findBlogById(id);
            logService.log(req, "admin-delete-blog", LogState.FAIL, LogLevel.ALERT, prevBlog, currentBlog);
            status = "error";
            notify = "Có lỗi khi xóa log!";
        } else {
            logService.log(req, "admin-delete-blog", LogState.SUCCESS, LogLevel.WARNING, prevBlog, null);
            status = "success";
            notify = "Xóa log thành công!";
            UserBean user = (UserBean) SessionUtil.getInstance().getValue(req, "user");
            SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), prevBlog.getId(), "Blog");
        }

        String jsonData = "{\"status\": \"" + status + "\", \"notify\": \"" + notify + "\"}";

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }
}
