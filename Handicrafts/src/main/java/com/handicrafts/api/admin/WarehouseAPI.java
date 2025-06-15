package com.handicrafts.api.admin;

import com.handicrafts.bean.ProductBean;
import com.handicrafts.bean.UserBean;
import com.handicrafts.bean.WarehouseBean;
import com.handicrafts.bean.WarehouseDetailBean;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dao.ProductDAO;
import com.handicrafts.dao.WarehouseDAO;
import com.handicrafts.dao.WarehouseDetailDAO;
import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.service.LogService;
import com.handicrafts.util.SendEmailUtil;
import com.handicrafts.util.SessionUtil;
import com.handicrafts.util.TransferDataUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = {"/api/admin/warehouse"})
public class WarehouseAPI extends HttpServlet {
    private final WarehouseDAO warehouseDAO = new WarehouseDAO();
    private WarehouseBean prevWarehouse;
    private final LogService<WarehouseBean> logService = new LogService<>();
    private final WarehouseDetailDAO warehouseDetailDAO = new WarehouseDetailDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy ra các Property mà DataTable gửi về
        // Thông tin về phân trang
        int draw = Integer.parseInt(req.getParameter("draw"));      // Số thứ tự của request hiện tại
        int start = Integer.parseInt(req.getParameter("start"));    // Vị trí bắt đầu của dữ liệu
        int length = Integer.parseInt(req.getParameter("length"));  // Số phần tử trên một trang

        // Thông tin về tìm kiếm
        String searchValue = req.getParameter("search[value]");

        // Thông tin về sắp xếp
        String orderBy = req.getParameter("order[0][column]") == null ? "0" : req.getParameter("order[0][column]");
        String orderDir = req.getParameter("order[0][dir]") == null ? "asc" : req.getParameter("order[0][dir]");
        String columnOrder = req.getParameter("columns[" + orderBy + "][data]");      // Tên của cột muốn sắp xếp

        List<WarehouseBean> warehouses = warehouseDAO.getWarehousesDatatable(start, length, columnOrder, orderDir, searchValue);
        int recordsTotal = warehouseDAO.getRecordsTotal();
        // Tổng số record khi filter search
        int recordsFiltered = warehouseDAO.getRecordsFiltered(searchValue);
        draw++;

        DatatableDTO<WarehouseBean> warehouseDatatableDTO = new DatatableDTO<>(warehouses, recordsTotal, recordsFiltered, draw);
        String jsonData = new TransferDataUtil<DatatableDTO<WarehouseBean>>().toJson(warehouseDatatableDTO);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String status;
        String notify;

        prevWarehouse = warehouseDAO.findWarehouseById(id);
        int affectedRow = warehouseDAO.deleteWarehouse(id);

        if (affectedRow < 1) {
            WarehouseBean currentWarehouse = warehouseDAO.findWarehouseById(id);
            logService.log(req, "admin-delete-warehouse", LogState.FAIL, LogLevel.ALERT, prevWarehouse, currentWarehouse);
            status = "error";
            notify = "Có lỗi khi xóa kho!";
        } else {
            List<WarehouseDetailBean> warehouseDetail = warehouseDetailDAO.findWarehouseDetailByWarehouseId(id);
            int count = 0;
            List<Integer> newQuantity = new ArrayList<>();
            List<Integer> productIds = new ArrayList<>();
            for (WarehouseDetailBean detail : warehouseDetail) {
                ProductBean product = productDAO.findProductById(detail.getProductId());
                if (detail.getQuantity() > product.getQuantity()) {
                    count++;
                } else {
                    newQuantity.add(product.getQuantity() - detail.getQuantity());
                    productIds.add(detail.getProductId());
                }
            }

            // Bằng 0 thì xóa được
            if (count == 0) {
                for (int i = 0; i < newQuantity.size(); i++) {
                    productDAO.updateQuantity(newQuantity.get(i), productIds.get(i));
                }
                logService.log(req, "admin-delete-warehouse", LogState.SUCCESS, LogLevel.WARNING, prevWarehouse, null);
                status = "success";
                notify = "Xóa kho thành công!";
                UserBean user = (UserBean) SessionUtil.getInstance().getValue(req, "user");
                SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), prevWarehouse.getId(), "Warehouse");
            } else {
                logService.log(req, "admin-delete-warehouse", LogState.FAIL, LogLevel.ALERT, prevWarehouse, null);
                status = "error";
                notify = "Số lượng hàng thực tế ít hơn nhập hàng, không thể hủy!";
            }
        }

        String jsonData = "{\"status\": \"" + status + "\", \"notify\": \"" + notify + "\"}";

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }
}
