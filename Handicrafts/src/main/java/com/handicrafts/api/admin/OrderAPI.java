package com.handicrafts.api.admin;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.dto.OrderDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.repository.OrderRepository;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.SendEmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/order")
public class OrderAPI {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ILogService<OrderDTO> logService;

    @PostMapping
    public ResponseEntity<DatatableDTO<OrderDTO>> getOrders(
            @RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("length") int length,
            @RequestParam(value = "search[value]", required = false) String searchValue,
            @RequestParam(value = "order[0][column]", required = false, defaultValue = "0") String orderColumnIndex,
            @RequestParam(value = "order[0][dir]", required = false, defaultValue = "asc") String orderDir,
            @RequestParam Map<String, String> allParams
    ) {
        // Lấy column name từ index
        String columnOrder = allParams.getOrDefault("columns[" + orderColumnIndex + "][data]", "id");

        List<OrderDTO> orders = orderRepository.getOrderDatatable(start, length, columnOrder, orderDir, searchValue);
        int recordsTotal = orderRepository.getRecordsTotal();
        int recordsFiltered = orderRepository.getRecordsFiltered(searchValue);

        DatatableDTO<OrderDTO> response = new DatatableDTO<>(orders, recordsTotal, recordsFiltered, draw + 1);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> cancelOrder(
            @RequestParam("id") int id,
            Authentication authentication
    ) {
        Map<String, String> response = new HashMap<>();

        OrderDTO prevOrder = orderRepository.findOrderById(id);
        int affected = orderRepository.cancelOrderAdmin(id);
        OrderDTO currentOrder = orderRepository.findOrderById(id);

        String status;
        String notify;

        if (affected < 1) {
            logService.log("admin-delete-order", LogState.FAIL, LogLevel.ALERT, prevOrder, currentOrder);
            status = "error";
            notify = "Có lỗi khi hủy đơn hàng!";
        } else {
            logService.log("admin-delete-order", LogState.SUCCESS, LogLevel.WARNING, prevOrder, currentOrder);
            status = "success";
            notify = "Hủy đơn hàng thành công!";

            // Nếu có user đăng nhập thì gửi email
            if (authentication != null && authentication.getPrincipal() instanceof UserDTO user) {
                SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), id, "Order");
            }
        }

        response.put("status", status);
        response.put("notify", notify);
        return ResponseEntity.ok(response);
    }
}
