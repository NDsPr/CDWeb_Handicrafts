package com.handicrafts.api.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.dto.WarehouseDetailDTO;
import com.handicrafts.repository.WarehouseDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/warehouse-detail")
public class WarehouseDetailAPI {

    @Autowired
    private WarehouseDetailRepository warehouseDetailRepository;

    @PostMapping
    public ResponseEntity<String> getWarehouseDetailDatatable(@RequestParam int warehouseId,
                                                              @RequestParam int draw,
                                                              @RequestParam int start,
                                                              @RequestParam int length,
                                                              @RequestParam("columns[0][data]") String orderColumn,
                                                              @RequestParam("order[0][dir]") String orderDir,
                                                              @RequestParam("search[value]") String searchValue) throws JsonProcessingException {

        List<WarehouseDetailDTO> details = warehouseDetailRepository.getWarehouseDetailsDatatable(warehouseId, start, length, orderColumn, orderDir, searchValue);
        int recordsTotal = warehouseDetailRepository.getRecordsTotal();
        int recordsFiltered = warehouseDetailRepository.getRecordsFiltered(warehouseId, searchValue);

        DatatableDTO<WarehouseDetailDTO> response = new DatatableDTO<>(details, recordsTotal, recordsFiltered, draw + 1);
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.ok(mapper.writeValueAsString(response));
    }
}
