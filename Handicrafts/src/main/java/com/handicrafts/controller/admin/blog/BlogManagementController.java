package com.handicrafts.controller.admin.blog;

import com.handicrafts.dto.BlogDTO;
import com.handicrafts.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/blog-management")
public class BlogManagementController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private Environment environment;

    @GetMapping("")
    public String showBlogManagement(
            Model model,
            @RequestParam(value = "draw", defaultValue = "1") int draw,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "length", defaultValue = "10") int length,
            @RequestParam(value = "order[0][column]", defaultValue = "0") int columnIndex,
            @RequestParam(value = "order[0][dir]", defaultValue = "asc") String direction,
            @RequestParam(value = "search[value]", required = false) String searchValue,
            @RequestParam(value = "success", required = false) String success,
            @RequestParam(value = "error", required = false) String error
    ) {
        try {
            // Xác định cột để sắp xếp
            String[] columns = {"id", "title", "author", "description", "content", "categoryId",
                    "status", "profilePic", "createdDate", "createdBy", "modifiedDate", "modifiedBy"};
            String columnOrder = columns[columnIndex];

            // Lấy danh sách blogs sử dụng phương thức datatable có sẵn
            List<BlogDTO> blogs = blogRepository.getBlogsDatatable(start, length, columnOrder, direction, searchValue);

            // Lấy tổng số lượng bản ghi và số lượng bản ghi được lọc
            int recordsTotal = blogRepository.getRecordsTotal();
            int recordsFiltered = blogRepository.getRecordsFiltered(searchValue);

            // Thêm dữ liệu vào model
            model.addAttribute("blogs", blogs);
            model.addAttribute("draw", draw);
            model.addAttribute("recordsTotal", recordsTotal);
            model.addAttribute("recordsFiltered", recordsFiltered);

            // Xử lý thông báo thành công/lỗi nếu có
            if (success != null) {
                String successMessage = environment.getProperty("ui-message.blog-operation-success", "Operation completed successfully");
                model.addAttribute("msg", successMessage);
            } else if (error != null) {
                String errorMessage = environment.getProperty("ui-message.blog-operation-error", "An error occurred");
                model.addAttribute("msg", errorMessage);
            }

            return "blog-management";
        } catch (Exception e) {
            String errorMessage = environment.getProperty("ui-message.blog-list-error", "Error loading blog list");
            model.addAttribute("msg", errorMessage);
            model.addAttribute("error", e.getMessage());
            return "blog-management";
        }
    }

    @GetMapping("/list")
    public String showAllBlogs(Model model) {
        try {
            List<BlogDTO> blogs = blogRepository.findAllBlogs();
            model.addAttribute("blogs", blogs);
            return "blog-list";
        } catch (Exception e) {
            String errorMessage = environment.getProperty("ui-message.blog-list-error", "Error loading blog list");
            model.addAttribute("msg", errorMessage);
            return "blog-list";
        }
    }
}
