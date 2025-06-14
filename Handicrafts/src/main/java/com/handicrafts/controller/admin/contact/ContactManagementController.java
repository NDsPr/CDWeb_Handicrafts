package com.handicrafts.controller.admin.contact;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/contact-management")
public class ContactManagementController extends HttpServlet {
//    private final ContactDAO contactDAO = new ContactDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<ContactBean> contacts = contactDAO.findAllContacts();
//        request.setAttribute("contacts", contacts);
        request.getRequestDispatcher("/contact-management.jsp").forward(request, response);
    }
}