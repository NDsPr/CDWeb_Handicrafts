package com.handicrafts.api.admin;

import com.handicrafts.bean.OrderBean;
import com.handicrafts.bean.UserDTO;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dao.OrderDAO;
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
import java.util.List;

@WebServlet(value ={ "/api/admin/order"})
public class OrderAPI extends HttpServlet {
    private  final OrderDAO orderDAO = new OrderDAO();
    private OrderBean prevOrder;
    private LogService<OrderBean> logService = new LogService<>();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
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

        List<OrderBean> orders = orderDAO.getOrderDatatable(start, length, columnOrder, orderDir, searchValue);
        int recordsTotal = orderDAO.getRecordsTotal();
        // Tổng số record khi filter search
        int recordsFiltered = orderDAO.getRecordsFiltered(searchValue);
        draw++;

        DatatableDTO<OrderBean> orderDatatableDTO = new DatatableDTO<>(orders, recordsTotal, recordsFiltered, draw);
        String jsonData = new TransferDataUtil<DatatableDTO<OrderBean>>().toJson(orderDatatableDTO);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String status;
        String notify;

        prevOrder = orderDAO.findOrderById(id);
        int affectedRow = orderDAO.cancerOrderAdmin(id);

        OrderBean currentOrder = orderDAO.findOrderById(id);
        if (affectedRow < 1) {
            logService.log(req, "admin-delete-order", LogState.FAIL, LogLevel.ALERT, prevOrder, currentOrder);
            status = "error";
            notify = "Có lỗi khi hủy đơn hàng!";
        } else {
            logService.log(req, "admin-delete-order", LogState.SUCCESS, LogLevel.WARNING, prevOrder, currentOrder);
            status = "success";
            notify = "Hủy đơn hàng thành công!";
            UserBean user = (UserBean) SessionUtil.getInstance().getValue(req, "user");
            SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), prevOrder.getId(), "Order");
        }

        String jsonData = "{\"status\": \"" + status + "\", \"notify\": \"" + notify + "\"}";

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }
}
