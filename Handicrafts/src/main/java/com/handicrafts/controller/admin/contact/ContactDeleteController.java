package com.handicrafts.controller.admin.contact;
import com.handicrafts.dao.ContactDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/contact-management/delete")
public class ContactDeleteController extends HttpServlet {
    private final ContactDAO contactDAO = new ContactDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int affectedRows = contactDAO.deleteContact(id);

        if (affectedRows > 0) {
            response.sendRedirect(request.getContextPath() + "/admin/contact-management?success=true");
        } else {
            // Xử lý lỗi nếu cần
            response.sendRedirect(request.getContextPath() + "/admin/contact-management?error=true");
        }
    }
}

