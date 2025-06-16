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
import java.util.ResourceBundle;

@Controller
@RequestMapping("/admin/warehouse-management/adding")
public class WarehouseAddingController {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ILogService<WarehouseDTO> logService;

    private final ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    @GetMapping
    public String showAddForm() {
        return "adding-warehouse"; // mapping đến adding-warehouse.jsp hoặc .html
    }

    @PostMapping
    public String handleAddWarehouse(@RequestParam("shippingFrom") String shippingFrom,
                                     @RequestParam("shippingStart") String shippingStart,
                                     @RequestParam("shippingDone") String shippingDone,
                                     @RequestParam("description") String description,
                                     @RequestParam("createdBy") String createdBy,
                                     Model model,
                                     HttpServletRequest request) {
        boolean isValid = true;
        String[] inputsForm = {shippingFrom, shippingStart, shippingDone, description, createdBy};
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        String msg;
        if (isValid) {
            Timestamp shippingStartTimestamp = Timestamp.valueOf(shippingStart);
            Timestamp shippingDoneTimestamp = Timestamp.valueOf(shippingDone);

            WarehouseDTO newWarehouse = new WarehouseDTO();
            newWarehouse.setShippingFrom(shippingFrom);
            newWarehouse.setShippingStart(shippingStartTimestamp);
            newWarehouse.setShippingDone(shippingDoneTimestamp);
            newWarehouse.setDescription(description);
            newWarehouse.setCreatedBy(createdBy);

            int id = warehouseRepository.createWarehouse(newWarehouse);
            if (id <= 0) {
                logService.log((jakarta.servlet.http.HttpServletRequest) request, "admin-add-warehouse", LogState.FAIL, LogLevel.ALERT, null, null);
                msg = "error";
            } else {
                WarehouseDTO currentWarehouse = warehouseRepository.findWarehouseById(id);
                logService.log((jakarta.servlet.http.HttpServletRequest) request, "admin-add-warehouse", LogState.SUCCESS, LogLevel.WARNING, null, currentWarehouse);
                msg = "success";
            }
        } else {
            model.addAttribute("errors", errors);
            msg = "error";
        }

        model.addAttribute("msg", msg);
        return "adding-warehouse";
    }
}
