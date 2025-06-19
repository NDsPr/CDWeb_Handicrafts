package com.handicrafts.controller.web;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.security.service.LinkVerifyService;
import com.handicrafts.service.ILogService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet(value = {"/user-change-password"})
public class ChangePasswordController extends HttpServlet {

    private LinkVerifyService linkVerifyService;

    private UserRepository userRepository;

    @Autowired
    private ILogService<UserEntity> logService;

    private final ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        linkVerifyService = new LinkVerifyService();
        userRepository = new UserRepository();
    }

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

        if (!linkVerifyService.isBlankInput(newPassword) && !linkVerifyService.isBlankInput(retypePassword)) {
            if (!linkVerifyService.containsSpace(newPassword) && !linkVerifyService.containsSpace(retypePassword)) {
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
                    isValid = false;
                }
            } else {
                if (linkVerifyService.containsSpace(newPassword)) {
                    newPasswordSpaceErr = "Mật khẩu không được chứa ô trống!";
                    req.setAttribute("newpw-error", newPasswordSpaceErr);
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

        UserEntity prevUser = userRepository.findByEmail(email);

        if (isValid) {
            int affectedRows = linkVerifyService.saveRenewPasswordByEmail(email, newPassword);
            UserEntity currentUser = userRepository.findByEmail(email);

            if (affectedRows <= 0) {
                logService.log(req, "user-change-password", LogState.FAIL.toString(), LogLevel.ALERT, prevUser, currentUser);
                String error = "e";
                req.setAttribute("error", error);
                req.getRequestDispatcher("/forget.jsp").forward(req, resp);
            } else {
                logService.log(req, "user-change-password", LogState.SUCCESS.toString(), LogLevel.WARNING, prevUser, currentUser);
                req.getRequestDispatcher("/thankyou.jsp").forward(req, resp);
            }
        } else {
            UserEntity currentUser = userRepository.findByEmail(email);
            logService.log(req, "user-change-password", LogState.FAIL.toString(), LogLevel.ALERT, prevUser, currentUser);
            req.getRequestDispatcher("/change-password.jsp").forward(req, resp);
        }
    }
}
