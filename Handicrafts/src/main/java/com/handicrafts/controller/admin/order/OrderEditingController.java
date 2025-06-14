package com.handicrafts.controller.admin.order;

import com.ltw.bean.OrderBean;
import com.ltw.bean.UserBean;
import com.ltw.constant.LogLevel;
import com.ltw.constant.LogState;
import com.ltw.dao.OrderDAO;
import com.ltw.dao.UserDAO;
import com.ltw.service.LogService;
import com.ltw.util.NumberValidateUtil;
import com.ltw.util.SendEmailUtil;
import com.ltw.util.ValidateParamUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet(value = {"/admin/order-management/editing"})
public class OrderEditingController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final UserDAO userDAO = new UserDAO();
    private LogService<OrderBean> logService = new LogService<>();
    private final ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        OrderBean orderBean = orderDAO.findOrderById(id);
        req.setAttribute("displayOrder", orderBean);
        req.getRequestDispatcher("/editing-order.jsp").forward(req, resp);
    }

    // TODO: Sửa lại code
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // Lấy id kiểu int ra để lưu vào database
        int id = Integer.parseInt(req.getParameter("id"));
        String shipToDate = req.getParameter("shipToDate");
        String status = req.getParameter("status");

        // Biến thông báo thành công
        String msg = "";

        String[] inputsForm = {shipToDate, status};
        // Biến bắt lỗi
        boolean isValid = true;

        // Kiểm tra input rằng/null trong hàm checkEmptyParam
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        // Nếu có lỗi (khác null) trả về isValid = false
        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        // Lấy ra Order trong Database làm giá trị previous
        OrderBean prevOrder = orderDAO.findOrderById(id);
        if (isValid) {
            java.util.Date parsedDate;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                parsedDate = dateFormat.parse(shipToDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Timestamp shipToDateConvert = new Timestamp(parsedDate.getTime());

            // Nếu không lỗi thì lưu vào database
            // Đổi String về số
            int statusInt = NumberValidateUtil.toInt(status);
            // Set thuộc tính vào bean
            OrderBean orderBean = new OrderBean();
            orderBean.setId(id);
            orderBean.setStatus(statusInt);
            orderBean.setShipToDate(shipToDateConvert);

            int affectedRows = orderDAO.updateOrder(orderBean);
            OrderBean currentOrder = orderDAO.findOrderById(id);
            if (affectedRows > 0) {
                logService.log(req, "admin-update-order", LogState.SUCCESS, LogLevel.WARNING, prevOrder, currentOrder);
                msg = "success";
                if (prevOrder.getStatus() != currentOrder.getStatus()) {
                    UserBean user = userDAO.findUserByOrderId(id);
                    SendEmailUtil.sendOrderNotify(user.getEmail(), currentOrder.getId(), currentOrder.getStatus());
                }
            } else {
                logService.log(req, "admin-update-order", LogState.FAIL, LogLevel.ALERT, prevOrder, currentOrder);
                msg = "fail";
            }
        } else {
            req.setAttribute("errors", errors);
            OrderBean currentOrder = orderDAO.findOrderById(id);
            logService.log(req, "admin-update-order", LogState.FAIL, LogLevel.ALERT, prevOrder, currentOrder);
            msg = "error";
        }

        OrderBean displayOrder = orderDAO.findOrderById(id);
        req.setAttribute("msg", msg);
        req.setAttribute("displayOrder", displayOrder);
        req.getRequestDispatcher("/editing-order.jsp").forward(req, resp);
    }
}
