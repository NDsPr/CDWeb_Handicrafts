package com.handicrafts.controller.admin.warehouse;

import com.ltw.bean.WarehouseBean;
import com.ltw.constant.LogLevel;
import com.ltw.constant.LogState;
import com.ltw.dao.WarehouseDAO;
import com.ltw.service.LogService;
import com.ltw.util.ValidateParamUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/admin/warehouse-management/editing")
public class WarehouseEditingController extends HttpServlet {
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();
    private LogService<WarehouseBean> logService = new LogService<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        WarehouseBean warehouseBean = warehouseDAO.findWarehouseById(id);
        request.setAttribute("warehouse", warehouseBean);
        request.getRequestDispatcher("/editing-warehouse.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("id");
        int id = Integer.parseInt(idStr);

        String shippingFrom = req.getParameter("shippingFrom");
        String shippingStartStr = req.getParameter("shippingStart");
        String shippingDoneStr = req.getParameter("shippingDone");
        String description = req.getParameter("description");
        String createdBy = req.getParameter("createdBy");

        String msg = "";
        String[] inputsForm = {shippingFrom, shippingStartStr, shippingDoneStr, description, createdBy};
        boolean isValid = true;

        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        WarehouseBean prevWarehouse = warehouseDAO.findWarehouseById(id);
        if (isValid) {
            Timestamp shippingStart = Timestamp.valueOf(shippingStartStr);
            Timestamp shippingDone = Timestamp.valueOf(shippingDoneStr);

            WarehouseBean nowChange = new WarehouseBean();
            nowChange.setId(id);
            nowChange.setShippingFrom(shippingFrom);
            nowChange.setShippingStart(shippingStart);
            nowChange.setShippingDone(shippingDone);
            nowChange.setDescription(description);
            nowChange.setCreatedBy(createdBy);

            int affectedRow = warehouseDAO.updateWarehouse(nowChange);
            WarehouseBean currentWarehouse = warehouseDAO.findWarehouseById(id);

            if (affectedRow < 0) {
                logService.log(req, "admin-update-warehouse", LogState.FAIL, LogLevel.ALERT, prevWarehouse, currentWarehouse);
                msg = "error";
            } else if (affectedRow > 0) {
                logService.log(req, "admin-update-warehouse", LogState.SUCCESS, LogLevel.WARNING, prevWarehouse, currentWarehouse);
                msg = "success";
            } else {
                msg = "error";
            }
        } else {
            req.setAttribute("errors", errors);
            WarehouseBean currentWarehouse = warehouseDAO.findWarehouseById(id);
            logService.log(req, "admin-update-warehouse", LogState.FAIL, LogLevel.ALERT, prevWarehouse, currentWarehouse);
            msg = "error";
        }

        WarehouseBean displayWarehouse = warehouseDAO.findWarehouseById(id);
        req.setAttribute("msg", msg);
        req.setAttribute("displayWarehouse", displayWarehouse);
        req.getRequestDispatcher("/editing-warehouse.jsp").forward(req, resp);
    }
}
