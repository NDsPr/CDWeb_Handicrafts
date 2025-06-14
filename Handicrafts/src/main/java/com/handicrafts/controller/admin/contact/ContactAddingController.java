package com.handicrafts.controller.admin.contact;

import com.ltw.bean.ContactBean;
import com.ltw.dao.ContactDAO;
import com.ltw.util.BlankInputUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/admin/contact-management/adding")
public class ContactAddingController extends HttpServlet {
    private final ContactDAO contactDAO = new ContactDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/adding-contact.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String message = req.getParameter("message");

        String success = "success";
        String[] inputsForm = new String[]{email, firstName, lastName, message};
        ArrayList<String> errors = new ArrayList<>();
        boolean isValid = true;

        for (String string : inputsForm) {
            if (BlankInputUtil.isBlank(string)) {
                errors.add("e");
                if (isValid) {
                    isValid = false;
                }
            } else {
                errors.add(null);
            }
        }
        req.setAttribute("errors", errors);

        if (isValid) {
            ContactBean contactBean = new ContactBean();
            contactBean.setEmail(email);
            contactBean.setFirstName(firstName);
            contactBean.setLastName(lastName);
            contactBean.setMessage(message);

            contactDAO.createContact(contactBean);
            resp.sendRedirect(req.getContextPath() + "/admin/contact-management/adding?success=" + success);
        } else {
            req.getRequestDispatcher("/adding-contact.jsp").forward(req, resp);
        }
    }
}
