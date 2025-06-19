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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/blog")
public class BlogAPI {

    private final BlogRepository blogRepository;
    private final ILogService<BlogDTO> logService;
    private BlogDTO prevBlog;

    @Autowired
    public BlogAPI(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
        this.logService = new LogServiceImp<>();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DatatableDTO<BlogDTO>> getBlogsDatatable(
            @RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("length") int length,
            @RequestParam(value = "search[value]", required = false) String searchValue,
            @RequestParam(value = "order[0][column]", defaultValue = "0") String orderBy,
            @RequestParam(value = "order[0][dir]", defaultValue = "asc") String orderDir,
            @RequestParam(value = "columns[" + "${orderBy}" + "][data]", required = false) String columnOrder) {

        // Nếu columnOrder là null (có thể xảy ra do cách Spring xử lý tham số), thì gán giá trị mặc định
        if (columnOrder == null) {
            columnOrder = "id"; // hoặc trường mặc định khác
        }

        List<BlogDTO> blogs = blogRepository.getBlogsDatatable(start, length, columnOrder, orderDir, searchValue);
        int recordsTotal = blogRepository.getRecordsTotal();
        int recordsFiltered = blogRepository.getRecordsFiltered(searchValue);

        draw++;

        DatatableDTO<BlogDTO> blogDatatableDTO = new DatatableDTO<>(blogs, recordsTotal, recordsFiltered, draw);

        return ResponseEntity.ok(blogDatatableDTO);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteBlog(
            @RequestParam("id") int id,
            HttpServletRequest request) {

        Map<String, String> response = new HashMap<>();
        String status;
        String notify;

        prevBlog = blogRepository.findBlogById(id);
        int affectedRow = blogRepository.deleteBlog(id);

        if (affectedRow < 1) {
            BlogDTO currentBlog = blogRepository.findBlogById(id);
            logService.log(request, "admin-delete-blog", LogState.FAIL, LogLevel.ALERT, prevBlog, currentBlog);
            status = "error";
            notify = "Có lỗi khi xóa blog!";
            response.put("status", status);
            response.put("notify", notify);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } else {
            logService.log(request, "admin-delete-blog", LogState.SUCCESS, LogLevel.WARNING, prevBlog, null);
            status = "success";
            notify = "Xóa blog thành công!";

            // Gửi thông báo email
            UserDTO user = (UserDTO) SessionUtil.getInstance().getValue(request, "user");
            SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), prevBlog.getId(), "Blog");

            response.put("status", status);
            response.put("notify", notify);
            return ResponseEntity.ok(response);
        }
    }
}
