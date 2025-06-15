package com.handicrafts.controller.admin.warehouse;

import com.handicrafts.bean.WarehouseBean;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dao.WarehouseDAO;
import com.handicrafts.service.LogService;
import com.handicrafts.util.ValidateParamUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet("/admin/warehouse-management/adding")
public class WarehouseAddingController extends HttpServlet {
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();
    private LogService<WarehouseBean> logService = new LogService<>();
    private ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/adding-warehouse.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String shippingFrom = req.getParameter("shippingFrom");
        String shippingStart = req.getParameter("shippingStart");
        String shippingDone = req.getParameter("shippingDone");
        String description = req.getParameter("description");
        String createdBy = req.getParameter("createdBy");

        String msg = null;
        String[] inputsForm = {shippingFrom, shippingStart, shippingDone, description, createdBy};
        // Biến bắt lỗi
        boolean isValid = true;

        // Kiểm tra input rỗng/null trong hàm checkEmptyParam
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        // Nếu có lỗi (khác null) trả về isValid = false
        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        // Nếu không lỗi thì lưu vào database cùng với
        if (isValid) {
            // Chuyển đổi các thông số ngày tháng
            Timestamp shippingStartTimestamp = Timestamp.valueOf(shippingStart);
            Timestamp shippingDoneTimestamp = Timestamp.valueOf(shippingDone);

            // Set thuộc tính vào bean
            WarehouseBean nowChange = new WarehouseBean();
            nowChange.setShippingFrom(shippingFrom);
            nowChange.setShippingStart(shippingStartTimestamp);
            nowChange.setShippingDone(shippingDoneTimestamp);
            nowChange.setDescription(description);
            nowChange.setCreatedBy(createdBy);

            int id = warehouseDAO.createWarehouse(nowChange);

            if (id <= 0) {
                logService.log(req, "admin-add-warehouse", LogState.FAIL, LogLevel.ALERT, null, null);
                msg = "error";
            } else {
                WarehouseBean currentWarehouse = warehouseDAO.findWarehouseById(id);
                logService.log(req, "admin-add-warehouse", LogState.SUCCESS, LogLevel.WARNING, null, currentWarehouse);
                msg = "success";
            }
        } else {
            // Lỗi nhập liệu người dùng thì không ghi log
            req.setAttribute("errors", errors);
            msg = "error";
        }

        req.setAttribute("msg", msg);
        req.getRequestDispatcher("/adding-warehouse.jsp").forward(req, resp);
    }
}
