package com.handicrafts.api.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.dto.ProductDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.dto.WarehouseDTO;
import com.handicrafts.dto.WarehouseDetailDTO;
import com.handicrafts.repository.ProductRepository;
import com.handicrafts.repository.WarehouseDetailRepository;
import com.handicrafts.repository.WarehouseRepository;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.SendEmailUtil;
import com.handicrafts.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/warehouse")
public class WarehouseAPI {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseDetailRepository warehouseDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ILogService<WarehouseDTO> logService;

    @PostMapping
    public ResponseEntity<String> getWarehouseDatatable(@RequestParam int draw,
                                                        @RequestParam int start,
                                                        @RequestParam int length,
                                                        @RequestParam("columns[0][data]") String orderColumn,
                                                        @RequestParam("order[0][dir]") String orderDir,
                                                        @RequestParam("search[value]") String searchValue) throws JsonProcessingException {

        List<WarehouseDTO> warehouses = warehouseRepository.getWarehousesDatatable(start, length, orderColumn, orderDir, searchValue);
        int recordsTotal = warehouseRepository.getRecordsTotal();
        int recordsFiltered = warehouseRepository.getRecordsFiltered(searchValue);

        DatatableDTO<WarehouseDTO> response = new DatatableDTO<>(warehouses, recordsTotal, recordsFiltered, draw + 1);
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.ok(mapper.writeValueAsString(response));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWarehouse(@RequestParam int id, HttpServletRequest request) {
        String status;
        String notify;

        WarehouseDTO prevWarehouse = warehouseRepository.findWarehouseById(id);
        int affectedRow = warehouseRepository.deleteWarehouse(id);

        if (affectedRow < 1) {
            WarehouseDTO currentWarehouse = warehouseRepository.findWarehouseById(id);
            logService.log(request, "admin-delete-warehouse", LogState.FAIL, LogLevel.ALERT, prevWarehouse, currentWarehouse);
            status = "error";
            notify = "Có lỗi khi xóa kho!";
        } else {
            List<WarehouseDetailDTO> warehouseDetails = warehouseDetailRepository.findWarehouseDetailByWarehouseId(id);
            int count = 0;
            List<Integer> newQuantities = new ArrayList<>();
            List<Integer> productIds = new ArrayList<>();

            for (WarehouseDetailDTO detail : warehouseDetails) {
                ProductDTO product = productRepository.findById(detail.getProductId());
                if (detail.getQuantity() > product.getQuantity()) {
                    count++;
                } else {
                    newQuantities.add(product.getQuantity() - detail.getQuantity());
                    productIds.add(detail.getProductId());
                }
            }

            if (count == 0) {
                for (int i = 0; i < newQuantities.size(); i++) {
                    productRepository.updateQuantity(productIds.get(i), newQuantities.get(i));
                }
                logService.log(request, "admin-delete-warehouse", LogState.SUCCESS, LogLevel.WARNING, prevWarehouse, null);
                status = "success";
                notify = "Xóa kho thành công!";

                UserDTO user = (UserDTO) SessionUtil.getInstance().getValue(request, "user");
                SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), prevWarehouse.getId(), "Warehouse");
            } else {
                logService.log(request, "admin-delete-warehouse", LogState.FAIL, LogLevel.ALERT, prevWarehouse, null);
                status = "error";
                notify = "Số lượng hàng thực tế ít hơn nhập hàng, không thể hủy!";
            }
        }

        String jsonData = String.format("{\"status\": \"%s\", \"notify\": \"%s\"}", status, notify);
        return ResponseEntity.ok(jsonData);
    }
}
