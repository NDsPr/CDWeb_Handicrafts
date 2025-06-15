package com.handicrafts.api.admin;

import com.handicrafts.dto.OrderDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.repository.OrderRepository;
import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.security.service.LogService;
import com.handicrafts.util.SendEmailUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin/order")
public class OrderAPI {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LogService<OrderDTO> logService;

    private OrderDTO prevOrder;

    @PostMapping
    public ResponseEntity<DatatableDTO<OrderDTO>> getOrders(
            @RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("length") int length,
            @RequestParam(value = "search[value]", required = false) String searchValue,
            @RequestParam(value = "order[0][column]", required = false, defaultValue = "0") String orderBy,
            @RequestParam(value = "order[0][dir]", required = false, defaultValue = "asc") String orderDir,
            @RequestParam(value = "columns[" + "${orderBy}" + "][data]", required = false) String columnOrder) {

        // If columnOrder is null (which can happen due to the dynamic parameter name), use a default value
        if (columnOrder == null) {
            columnOrder = "id"; // Default column for sorting
        }

        List<OrderDTO> orders = orderRepository.getOrderDatatable(start, length, columnOrder, orderDir, searchValue);
        int recordsTotal = orderRepository.getRecordsTotal();
        int recordsFiltered = orderRepository.getRecordsFiltered(searchValue);
        draw++;

        DatatableDTO<OrderDTO> orderDatatableDTO = new DatatableDTO<>(orders, recordsTotal, recordsFiltered, draw);

        return ResponseEntity.ok(orderDatatableDTO);
    }

//    @DeleteMapping
//    public ResponseEntity<Map<String, String>> cancelOrder(
//            @RequestParam("id") int id,
//            Authentication authentication) {
//
//        Map<String, String> response = new HashMap<>();
//        String status;
//        String notify;
//
//        prevOrder = orderRepository.findOrderById(id);
//        int affectedRow = orderRepository.cancelOrderAdmin(id);
//
//        OrderDTO currentOrder = orderRepository.findOrderById(id);
//
//        if (affectedRow < 1) {
//            // Fix: Remove 'request:' prefix and match parameter order
//            logService.log("admin-delete-order", LogState.FAIL, LogLevel.ALERT, prevOrder, currentOrder);
//            status = "error";
//            notify = "Có lỗi khi hủy đơn hàng!";
//        } else {
//            // Fix: Remove 'request:' prefix and match parameter order
//            logService.log("admin-delete-order", LogState.SUCCESS, LogLevel.WARNING, prevOrder, currentOrder);
//            status = "success";
//            notify = "Hủy đơn hàng thành công!";
//
//            // Get current authenticated user
//            UserDTO user = (UserDTO) authentication.getPrincipal();
//            SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), prevOrder.getId(), "Order");
//        }
//
//        response.put("status", status);
//        response.put("notify", notify);
//
//        return ResponseEntity.ok(response);
//    }

}
