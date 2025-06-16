package com.handicrafts.controller.admin.warehouse;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.WarehouseDTO;
import com.handicrafts.repository.WarehouseRepository;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.ValidateParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/admin/warehouse-management/editing")
public class WarehouseEditingController {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ILogService<WarehouseDTO> logService;

    @GetMapping
    public String showEditPage(@RequestParam("id") int id, Model model) {
        WarehouseDTO warehouse = warehouseRepository.findWarehouseById(id);
        model.addAttribute("warehouse", warehouse);
        return "editing-warehouse";
    }

    @PostMapping
    public String handleUpdate(@RequestParam("id") int id,
                               @RequestParam("shippingFrom") String shippingFrom,
                               @RequestParam("shippingStart") String shippingStartStr,
                               @RequestParam("shippingDone") String shippingDoneStr,
                               @RequestParam("description") String description,
                               @RequestParam("createdBy") String createdBy,
                               HttpServletRequest request,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        boolean isValid = true;
        String[] inputsForm = {shippingFrom, shippingStartStr, shippingDoneStr, description, createdBy};
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        WarehouseDTO prevWarehouse = warehouseRepository.findWarehouseById(id);
        String msg;

        if (isValid) {
            Timestamp shippingStart = Timestamp.valueOf(shippingStartStr);
            Timestamp shippingDone = Timestamp.valueOf(shippingDoneStr);

            WarehouseDTO updated = new WarehouseDTO();
            updated.setId(id);
            updated.setShippingFrom(shippingFrom);
            updated.setShippingStart(shippingStart);
            updated.setShippingDone(shippingDone);
            updated.setDescription(description);
            updated.setCreatedBy(createdBy);

            int affected = warehouseRepository.updateWarehouse(updated);
            WarehouseDTO currentWarehouse = warehouseRepository.findWarehouseById(id);

            if (affected < 0) {
                logService.log((jakarta.servlet.http.HttpServletRequest) request, "admin-update-warehouse", LogState.FAIL, LogLevel.ALERT, prevWarehouse, currentWarehouse);
                msg = "error";
            } else {
                logService.log((jakarta.servlet.http.HttpServletRequest) request, "admin-update-warehouse", LogState.SUCCESS, LogLevel.WARNING, prevWarehouse, currentWarehouse);
                msg = "success";
            }
        } else {
            model.addAttribute("errors", errors);
            WarehouseDTO currentWarehouse = warehouseRepository.findWarehouseById(id);
            logService.log((jakarta.servlet.http.HttpServletRequest) request, "admin-update-warehouse", LogState.FAIL, LogLevel.ALERT, prevWarehouse, currentWarehouse);
            msg = "error";
        }

        WarehouseDTO displayWarehouse = warehouseRepository.findWarehouseById(id);
        model.addAttribute("displayWarehouse", displayWarehouse);
        model.addAttribute("msg", msg);
        return "editing-warehouse";
    }
}
