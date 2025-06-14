package com.handicrafts.controller.admin.contact;

import com.ltw.bean.ContactBean;
import com.ltw.dao.ContactDAO;
import com.ltw.util.NumberValidateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/contact-management/editing")
public class ContactEditingController extends HttpServlet {
    private final ContactDAO contactDAO = new ContactDAO();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ContactBean contactBean = contactDAO.findContactById(id);
        request.setAttribute("contactBean", contactBean);
        request.getRequestDispatcher("/editing-contact.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // Sử dụng vòng lặp để set lỗi để trống theo index,
        // tuy nhiên cần phải giữ đúng thứ tự của input theo form và theo database (Vì sử dụng vòng lặp theo i để set lỗi)
        String idStr = req.getParameter("id");
        // Lấy id kiểu int ra để lưu vào database
        int id = Integer.parseInt(idStr);
        String email = req.getParameter("email");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String message = req.getParameter("message");
        String status = req.getParameter("status");


        // Biến thông báo thành công
        String success = "success";
        // Đổi String về số
        int statusInt = NumberValidateUtil.toInt(status);

        // Set thuộc tính vào bean
        ContactBean contactBean = new ContactBean();
        contactBean.setEmail(email);
        contactBean.setFirstName(firstName);
        contactBean.setLastName(lastName);
        contactBean.setMessage(message);
        contactBean.setStatus(Integer.parseInt(status));

        contactDAO.updateContact(contactBean);

        contactBean.setId(id);
        resp.sendRedirect(req.getContextPath() + "/admin/contact-management/editing?id=" + contactBean.getId() + "&success=" + success);
    }
}