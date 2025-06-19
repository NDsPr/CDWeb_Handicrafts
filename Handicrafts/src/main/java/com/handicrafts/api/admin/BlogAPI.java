package com.handicrafts.api.admin;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.BlogDTO;
import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.repository.BlogRepository;
import com.handicrafts.service.ILogService;
import com.handicrafts.service.impl.LogServiceImp;
import com.handicrafts.util.SendEmailUtil;
import com.handicrafts.util.SessionUtil;
import com.handicrafts.util.TransferDataUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = { "/api/admin/blog" })
public class BlogAPI extends HttpServlet {

    private final BlogRepository blogRepository = new BlogRepository();
    private final ILogService<BlogDTO> logService = new LogServiceImp<>();
    private BlogDTO prevBlog;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Phân trang
        int draw = Integer.parseInt(req.getParameter("draw"));
        int start = Integer.parseInt(req.getParameter("start"));
        int length = Integer.parseInt(req.getParameter("length"));

        // Tìm kiếm
        String searchValue = req.getParameter("search[value]");

        // Sắp xếp
        String orderBy = req.getParameter("order[0][column]") == null ? "0" : req.getParameter("order[0][column]");
        String orderDir = req.getParameter("order[0][dir]") == null ? "asc" : req.getParameter("order[0][dir]");
        String columnOrder = req.getParameter("columns[" + orderBy + "][data]");

        // Lấy dữ liệu
        List<BlogDTO> blogs = blogRepository.getBlogsDatatable(start, length, columnOrder, orderDir, searchValue);
        int recordsTotal = blogRepository.getRecordsTotal();
        int recordsFiltered = blogRepository.getRecordsFiltered(searchValue);

        draw++;

        DatatableDTO<BlogDTO> blogDatatableDTO = new DatatableDTO<>(blogs, recordsTotal, recordsFiltered, draw);
        String jsonData = new TransferDataUtil<DatatableDTO<BlogDTO>>().toJson(blogDatatableDTO);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String status;
        String notify;

        prevBlog = blogRepository.findBlogById(id);
        int affectedRow = blogRepository.deleteBlog(id);

        if (affectedRow < 1) {
            BlogDTO currentBlog = blogRepository.findBlogById(id);
            logService.log(req, "admin-delete-blog", LogState.FAIL, LogLevel.ALERT, prevBlog, currentBlog);
            status = "error";
            notify = "Có lỗi khi xóa blog!";
        } else {
            logService.log(req, "admin-delete-blog", LogState.SUCCESS, LogLevel.WARNING, prevBlog, null);
            status = "success";
            notify = "Xóa blog thành công!";

            // Gửi thông báo email
            UserDTO user = (UserDTO) SessionUtil.getInstance().getValue(req, "user");
            SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), prevBlog.getId(), "Blog");
        }

        String jsonData = "{\"status\": \"" + status + "\", \"notify\": \"" + notify + "\"}";

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }
}
