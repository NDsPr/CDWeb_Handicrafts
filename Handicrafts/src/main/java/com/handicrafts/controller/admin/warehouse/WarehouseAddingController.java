package com.handicrafts.controller.admin.warehouse;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.WarehouseDTO;
import com.handicrafts.repository.WarehouseRepository;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.ValidateParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("${warehouse.management.adding.url}")
public class WarehouseAddingController {

    private final WarehouseRepository warehouseRepository;
    private final ILogService<WarehouseDTO> logService;

    @Value("${warehouse.adding.view}")
    private String addingWarehouseView;

    @Value("${warehouse.adding.log.action}")
    private String warehouseAddLogAction;

    @Value("${message.error}")
    private String errorMessage;

    @Value("${message.success}")
    private String successMessage;

    @Value("${model.attribute.message}")
    private String messageAttribute;

    @Autowired
    public WarehouseAddingController(WarehouseRepository warehouseRepository,
                                     ILogService<WarehouseDTO> logService) {
        this.warehouseRepository = warehouseRepository;
        this.logService = logService;
    }

    @GetMapping
    public String showAddForm() {
        return addingWarehouseView;
    }

    @PostMapping
    public String handleAddWarehouse(@RequestParam("shippingFrom") String shippingFrom,
                                     @RequestParam("shippingStart") String shippingStart,
                                     @RequestParam("shippingDone") String shippingDone,
                                     @RequestParam("description") String description,
                                     @RequestParam("createdBy") String createdBy,
                                     Model model,
                                     HttpServletRequest request) {
        // Validate input parameters
        ValidationResult validationResult = validateWarehouseInputs(shippingFrom, shippingStart,
                shippingDone, description, createdBy);

        String msg;
        if (validationResult.isValid()) {
            // Create and save warehouse
            WarehouseDTO newWarehouse = createWarehouseFromInputs(shippingFrom, shippingStart,
                    shippingDone, description, createdBy);

            int id = warehouseRepository.createWarehouse(newWarehouse);
            if (id <= 0) {
                logService.log(request, warehouseAddLogAction, LogState.FAIL, LogLevel.ALERT, null, null);
                msg = errorMessage;
            } else {
                WarehouseDTO currentWarehouse = warehouseRepository.findWarehouseById(id);
                logService.log(request, warehouseAddLogAction, LogState.SUCCESS, LogLevel.WARNING, null, currentWarehouse);
                msg = successMessage;
            }
        } else {
            model.addAttribute("errors", validationResult.getErrors());
            msg = errorMessage;
        }

        model.addAttribute(messageAttribute, msg);
        return addingWarehouseView;
    }

    private ValidationResult validateWarehouseInputs(String shippingFrom, String shippingStart,
                                                     String shippingDone, String description, String createdBy) {
        ValidationResult result = new ValidationResult();
        String[] inputsForm = {shippingFrom, shippingStart, shippingDone, description, createdBy};
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        boolean isValid = errors.stream().noneMatch(e -> e != null);

        // Additional validation can be added here
        // For example, validate date formats, check if shipping start is before shipping done, etc.

        result.setValid(isValid);
        result.setErrors(errors);
        return result;
    }

    private WarehouseDTO createWarehouseFromInputs(String shippingFrom, String shippingStart,
                                                   String shippingDone, String description, String createdBy) {
        Timestamp shippingStartTimestamp = Timestamp.valueOf(shippingStart);
        Timestamp shippingDoneTimestamp = Timestamp.valueOf(shippingDone);

        WarehouseDTO newWarehouse = new WarehouseDTO();
        newWarehouse.setShippingFrom(shippingFrom);
        newWarehouse.setShippingStart(shippingStartTimestamp);
        newWarehouse.setShippingDone(shippingDoneTimestamp);
        newWarehouse.setDescription(description);
        newWarehouse.setCreatedBy(createdBy);

        return newWarehouse;
    }

    // Inner class for validation results
    private static class ValidationResult {
        private boolean valid;
        private List<String> errors;

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
    }
}
