package com.handicrafts.api.admin;

import com.handicrafts.bean.WarehouseDetailBean;
import com.handicrafts.dao.WarehouseDetailDAO;
import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.util.TransferDataUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = {"/api/admin/warehouse-detail"})
public class WarehouseDetailAPI extends HttpServlet {
    private final WarehouseDetailDAO warehouseDetailDAO = new WarehouseDetailDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy ra các Property mà DataTable gửi về
        // Thông tin về phân trang
        int warehouseId = Integer.parseInt(req.getParameter("warehouseId"));
        int draw = Integer.parseInt(req.getParameter("draw"));      // Số thứ tự của request hiện tại
        int start = Integer.parseInt(req.getParameter("start"));    // Vị trí bắt đầu của dữ liệu
        int length = Integer.parseInt(req.getParameter("length"));  // Số phần tử trên một trang

        // Thông tin về tìm kiếm
        String searchValue = req.getParameter("search[value]");

        // Thông tin về sắp xếp
        String orderBy = req.getParameter("order[0][column]") == null ? "0" : req.getParameter("order[0][column]");
        String orderDir = req.getParameter("order[0][dir]") == null ? "asc" : req.getParameter("order[0][dir]");
        String columnOrder = req.getParameter("columns[" + orderBy + "][data]");      // Tên của cột muốn sắp xếp

        List<WarehouseDetailBean> warehouseDetails = warehouseDetailDAO.getWarehouseDetailsDatatable(warehouseId, start, length, columnOrder, orderDir, searchValue);
        int recordsTotal = warehouseDetailDAO.getRecordsTotal();
        // Tổng số record khi filter search
        int recordsFiltered = warehouseDetailDAO.getRecordsFiltered(warehouseId, searchValue);
        draw++;

        DatatableDTO<WarehouseDetailBean> warehouseDetailDatatableDTO = new DatatableDTO<>(warehouseDetails, recordsTotal, recordsFiltered, draw);
        String jsonData = new TransferDataUtil<DatatableDTO<WarehouseDetailBean>>().toJson(warehouseDetailDatatableDTO);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }
}
