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
@RequestMapping("${warehouse.management.editing.url}")
public class WarehouseEditingController {

    private final WarehouseRepository warehouseRepository;
    private final ILogService<WarehouseDTO> logService;

    @Value("${warehouse.editing.view}")
    private String editingWarehouseView;

    @Value("${warehouse.editing.log.action}")
    private String warehouseEditLogAction;

    @Value("${message.error}")
    private String errorMessage;

    @Value("${message.success}")
    private String successMessage;

    @Value("${model.attribute.message}")
    private String messageAttribute;

    @Value("${model.attribute.warehouse}")
    private String warehouseAttribute;

    @Value("${model.attribute.errors}")
    private String errorsAttribute;

    @Autowired
    public WarehouseEditingController(WarehouseRepository warehouseRepository,
                                      ILogService<WarehouseDTO> logService) {
        this.warehouseRepository = warehouseRepository;
        this.logService = logService;
    }

    @GetMapping
    public String showEditPage(@RequestParam("id") int id, Model model) {
        WarehouseDTO warehouse = warehouseRepository.findWarehouseById(id);
        model.addAttribute(warehouseAttribute, warehouse);
        return editingWarehouseView;
    }

    @PostMapping
    public String handleUpdate(@RequestParam("id") int id,
                               @RequestParam("shippingFrom") String shippingFrom,
                               @RequestParam("shippingStart") String shippingStartStr,
                               @RequestParam("shippingDone") String shippingDoneStr,
                               @RequestParam("description") String description,
                               @RequestParam("createdBy") String createdBy,
                               HttpServletRequest request,
                               Model model) {

        // Get previous warehouse state for logging
        WarehouseDTO prevWarehouse = warehouseRepository.findWarehouseById(id);

        // Validate input parameters
        ValidationResult validationResult = validateWarehouseInputs(shippingFrom, shippingStartStr,
                shippingDoneStr, description, createdBy);

        String msg;
        if (validationResult.isValid()) {
            // Create updated warehouse object
            WarehouseDTO updated = createUpdatedWarehouse(id, shippingFrom, shippingStartStr,
                    shippingDoneStr, description, createdBy);

            // Perform update
            int affected = warehouseRepository.updateWarehouse(updated);
            WarehouseDTO currentWarehouse = warehouseRepository.findWarehouseById(id);

            if (affected < 0) {
                logService.log(request, warehouseEditLogAction, LogState.FAIL,
                        LogLevel.ALERT, prevWarehouse, currentWarehouse);
                msg = errorMessage;
            } else {
                logService.log(request, warehouseEditLogAction, LogState.SUCCESS,
                        LogLevel.WARNING, prevWarehouse, currentWarehouse);
                msg = successMessage;
            }
        } else {
            model.addAttribute(errorsAttribute, validationResult.getErrors());
            WarehouseDTO currentWarehouse = warehouseRepository.findWarehouseById(id);
            logService.log(request, warehouseEditLogAction, LogState.FAIL,
                    LogLevel.ALERT, prevWarehouse, currentWarehouse);
            msg = errorMessage;
        }

        // Prepare model for view rendering
        WarehouseDTO displayWarehouse = warehouseRepository.findWarehouseById(id);
        model.addAttribute(warehouseAttribute, displayWarehouse);
        model.addAttribute(messageAttribute, msg);

        return editingWarehouseView;
    }

    private ValidationResult validateWarehouseInputs(String shippingFrom, String shippingStartStr,
                                                     String shippingDoneStr, String description, String createdBy) {
        ValidationResult result = new ValidationResult();
        String[] inputsForm = {shippingFrom, shippingStartStr, shippingDoneStr, description, createdBy};
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        boolean isValid = errors.stream().noneMatch(e -> e != null);

        // Additional validation can be added here
        // For example, validate date formats, check if shipping start is before shipping done, etc.

        result.setValid(isValid);
        result.setErrors(errors);
        return result;
    }

    private WarehouseDTO createUpdatedWarehouse(int id, String shippingFrom, String shippingStartStr,
                                                String shippingDoneStr, String description, String createdBy) {
        Timestamp shippingStart = Timestamp.valueOf(shippingStartStr);
        Timestamp shippingDone = Timestamp.valueOf(shippingDoneStr);

        WarehouseDTO updated = new WarehouseDTO();
        updated.setId(id);
        updated.setShippingFrom(shippingFrom);
        updated.setShippingStart(shippingStart);
        updated.setShippingDone(shippingDone);
        updated.setDescription(description);
        updated.setCreatedBy(createdBy);

        return updated;
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
