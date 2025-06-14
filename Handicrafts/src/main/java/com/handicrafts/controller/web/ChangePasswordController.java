package com.handicrafts.controller.web;

import com.ltw.bean.UserBean;
import com.ltw.constant.LogLevel;
import com.ltw.constant.LogState;
import com.ltw.dao.UserDAO;
import com.ltw.service.LinkVerifyService;
import com.ltw.service.LogService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet(value = {"/user-change-password"})
public class ChangePasswordController extends HttpServlet {
    private final LinkVerifyService linkVerifyService = new LinkVerifyService();
    private UserDAO userDAO = new UserDAO();
    private LogService<UserBean> logService = new LogService<>();
    private ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/change-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String newPassword = req.getParameter("newPassword");
        String retypePassword = req.getParameter("retypePassword");
        String newPasswordInputErr = null;
        String retypePasswordInputErr = null;
        String newPasswordSpaceErr = null;
        String retypePasswordSpaceErr = null;

        boolean isValid = true;

        if (!linkVerifyService.isBlankInput(newPassword) || !linkVerifyService.isBlankInput(retypePassword)) {
            if (!linkVerifyService.containsSpace(newPassword) || !linkVerifyService.containsSpace(retypePassword)) {
                if (linkVerifyService.isLengthEnough(newPassword)) {
                    if (!newPassword.equals(retypePassword)) {
                        newPasswordInputErr = "Mật khẩu và Nhập lại mật khẩu không đúng!";
                        retypePasswordInputErr = "Mật khẩu và Nhập lại mật khẩu không đúng!";
                        req.setAttribute("newpw-error", newPasswordInputErr);
                        req.setAttribute("retypePasswordInputErr", retypePasswordInputErr);
                        isValid = false;
                    }
                } else {
                    newPasswordInputErr = "Mật khẩu chứa ít nhất 6 ký tự!";
                    req.setAttribute("newpw-error", newPasswordInputErr);
                }
            } else {
                if (linkVerifyService.containsSpace(newPassword)) {
                    newPasswordSpaceErr = "Mật khẩu không được chứa ô trống!";
                    req.setAttribute("retype-error", newPasswordSpaceErr);
                }
                if (linkVerifyService.containsSpace(retypePassword)) {
                    retypePasswordSpaceErr = "Mật khẩu không được chứa ô trống!";
                    req.setAttribute("retype-error", retypePasswordSpaceErr);
                }
                isValid = false;
            }
        } else {
            if (linkVerifyService.isBlankInput(newPassword)) {
                newPasswordInputErr = "Mật khẩu không được để trống!";
                req.setAttribute("newpw-error", newPasswordInputErr);
            }
            if (linkVerifyService.isBlankInput(retypePassword)) {
                retypePasswordInputErr = "Mật khẩu không được để trống!";
                req.setAttribute("retype-error", retypePasswordInputErr);
            }
            isValid = false;
        }

        UserBean prevUser = userDAO.findUserByEmail(email);
        if (!isValid) {
            int affectedRows = linkVerifyService.saveRenewPasswordByEmail(email, newPassword);
            UserBean currentUser = userDAO.findUserByEmail(email);

            if (affectedRows < 0) {
                logService.log(req, "user-change-password", LogState.FAIL, LogLevel.ALERT, prevUser, currentUser);
                String error = "e";
                req.setAttribute("error", error);
                req.getRequestDispatcher("/forget.jsp").forward(req, resp);
            } else if (affectedRows > 0) {
                logService.log(req, "user-change-password", LogState.SUCCESS, LogLevel.WARNING, prevUser, currentUser);
                req.getRequestDispatcher("/thankyou.jsp").forward(req, resp);
            }
        } else {
            UserBean currentUser = userDAO.findUserByEmail(email);
            logService.log(req, "user-change-password", LogState.FAIL, LogLevel.ALERT, prevUser, currentUser);
            String error = "e";
            req.setAttribute("error", error);
            req.getRequestDispatcher("/forget.jsp").forward(req, resp);
        }
    }
}
