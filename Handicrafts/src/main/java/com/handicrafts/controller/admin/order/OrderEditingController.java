package com.handicrafts.controller.admin.order;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.OrderDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.OrderEntity;
import com.handicrafts.repository.OrderRepository;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.NumberValidateUtil;
import com.handicrafts.util.SendEmailUtil;
import com.handicrafts.util.ValidateParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/order-management/editing")
public class OrderEditingController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ILogService<OrderDTO> logService;

    @Autowired
    private Environment environment;

    @GetMapping
    public String getEditForm(@RequestParam("id") int id, Model model) {
        Optional<OrderEntity> order = orderRepository.findById(id);
        model.addAttribute(
                environment.getProperty("model.attribute.display-order", "displayOrder"),
                order
        );
        return environment.getProperty("order.view.editing");
    }

    @PostMapping
    public String editOrder(@RequestParam("id") int id,
                            @RequestParam("shipToDate") @DateTimeFormat(pattern = "yyyy-MM-dd") String shipToDate,
                            @RequestParam("status") String status,
                            Model model,
                            HttpServletRequest request) {

        String[] inputsForm = {shipToDate, status};
        boolean isValid = true;
        String msg;

        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);
        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        Optional<OrderEntity> prevOrder = orderRepository.findById(id);

        if (isValid) {
            Timestamp shipDate;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        environment.getProperty("date.format.pattern", "yyyy-MM-dd"));
                shipDate = new Timestamp(dateFormat.parse(shipToDate).getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            int statusInt = NumberValidateUtil.toInt(status);

            OrderDTO updatedOrder = new OrderDTO();
            updatedOrder.setId(id);
            updatedOrder.setStatus(statusInt);
            updatedOrder.setShipToDate(shipDate);

            int affectedRows = orderRepository.updateOrder(updatedOrder.toEntity());
            Optional<OrderEntity> currentOrder = orderRepository.findById(id);

//            if (affectedRows > 0) {
//                logService.log(request, environment.getProperty("log.action.update-order"),
//                               LogState.SUCCESS, LogLevel.WARNING, prevOrder, currentOrder);
//                msg = environment.getProperty("message.success", "success");
//                if (prevOrder.getStatus() != currentOrder.getStatus()) {
//                    UserDTO user = userRepository.findUserByOrderId(id);
//                    SendEmailUtil.sendOrderNotify(user.getEmail(), currentOrder.getId(), currentOrder.getStatus());
//                }
//            } else {
//                logService.log(request, environment.getProperty("log.action.update-order"),
//                               LogState.FAIL, LogLevel.ALERT, prevOrder, currentOrder);
//                msg = environment.getProperty("message.fail", "fail");
//            }
//        } else {
//            OrderDTO currentOrder = orderRepository.findOrderById(id);
//            model.addAttribute(
//                environment.getProperty("model.attribute.errors", "errors"),
//                errors
//            );
//            logService.log(request, environment.getProperty("log.action.update-order"),
//                           LogState.FAIL, LogLevel.ALERT, prevOrder, currentOrder);
//            msg = environment.getProperty("message.error", "error");
        }

        Optional<OrderEntity> displayOrder = orderRepository.findById(id);
        //model.addAttribute(environment.getProperty("model.attribute.message", "msg"), msg);
        model.addAttribute(
                environment.getProperty("model.attribute.display-order", "displayOrder"),
                displayOrder
        );

        return environment.getProperty("order.view.editing");
    }
}
