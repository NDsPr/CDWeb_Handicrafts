package com.handicrafts.api.admin;

import com.ltw.bean.ContactBean;
import com.ltw.bean.UserBean;
import com.ltw.constant.LogLevel;
import com.ltw.constant.LogState;
import com.ltw.dao.ContactDAO;
import com.ltw.dto.DatatableDTO;
import com.ltw.service.LogService;
import com.ltw.util.SendEmailUtil;
import com.ltw.util.SessionUtil;
import com.ltw.util.TransferDataUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = {"/api/admin/contact"})
public class ContactAPI extends HttpServlet {
    private final ContactDAO contactDAO = new ContactDAO();
    private ContactBean prevContact;
    private LogService<ContactBean> logService = new LogService<>();

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

        List<ContactBean> contacts = contactDAO.getContactsDatatable(start, length, columnOrder, orderDir, searchValue);
        int recordsTotal = contactDAO.getRecordsTotal();
        // Tổng số record khi filter search
        int recordsFiltered = contactDAO.getRecordsFiltered(searchValue);
        draw++;

        DatatableDTO<ContactBean> contactDatatableDTO = new DatatableDTO<>(contacts, recordsTotal, recordsFiltered, draw);
        String jsonData = new TransferDataUtil<DatatableDTO<ContactBean>>().toJson(contactDatatableDTO);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String status;
        String notify;

        prevContact = contactDAO.findContactById(id);
        int affectedRow = contactDAO.deleteContact(id);

        if (affectedRow < 1) {
            ContactBean currentContact = contactDAO.findContactById(id);
            logService.log(req, "admin-delete-contact", LogState.FAIL, LogLevel.ALERT, prevContact, currentContact);
            status = "error";
            notify = "Có lỗi khi xóa log!";
        } else {
            logService.log(req, "admin-delete-contact", LogState.SUCCESS, LogLevel.WARNING, prevContact, null);
            status = "success";
            notify = "Xóa log thành công!";
            UserBean user = (UserBean) SessionUtil.getInstance().getValue(req, "user");
            SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), prevContact.getId(), "Contact");
        }

        String jsonData = "{\"status\": \"" + status + "\", \"notify\": \"" + notify + "\"}";

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonData);
    }
}
