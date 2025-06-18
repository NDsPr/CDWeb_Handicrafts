package com.handicrafts.api.admin;

import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.dto.LogDTO;
import com.handicrafts.repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/log")
@RequiredArgsConstructor
public class LogAPI {

    private final LogRepository logRepository;

    @PostMapping
    public ResponseEntity<?> getLogs(HttpServletRequest req) {
        int draw = Integer.parseInt(req.getParameter("draw"));
        int start = Integer.parseInt(req.getParameter("start"));
        int length = Integer.parseInt(req.getParameter("length"));
        String searchValue = req.getParameter("search[value]");
        String orderBy = req.getParameter("order[0][column]") == null ? "0" : req.getParameter("order[0][column]");
        String orderDir = req.getParameter("order[0][dir]") == null ? "asc" : req.getParameter("order[0][dir]");
        String columnOrder = req.getParameter("columns[" + orderBy + "][data]");

        // Hiện tại chưa có chức năng lọc/sắp xếp trong repository JDBC nên tạm lấy toàn bộ
        List<LogDTO> logs = logRepository.findAll();
        int recordsTotal = logs.size();
        int recordsFiltered = logs.size(); // Có thể điều chỉnh sau nếu có tìm kiếm

        draw++;

        DatatableDTO<LogDTO> logDatatableDTO = new DatatableDTO<>(logs, recordsTotal, recordsFiltered, draw);
        return ResponseEntity.ok(logDatatableDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteLog(@RequestParam("id") int id) {
        try {
            logRepository.deleteById(id);
            return ResponseEntity.ok("{\"status\": \"success\", \"notify\": \"Xóa log thành công!\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"status\": \"error\", \"notify\": \"Có lỗi khi xóa log!\"}");
        }
    }
}
