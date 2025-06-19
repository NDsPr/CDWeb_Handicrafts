package com.handicrafts.controller.signin_signup_forget.via_page;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.security.service.LinkVerifyService;
import com.handicrafts.service.ILogService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet(value = {"/change-password"})
public class ChangePwController extends HttpServlet {

    @Autowired
    private ILogService<UserDTO> logService;

    private final LinkVerifyService linkVerifyService = new LinkVerifyService();
    private final ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    @Override
    public void init() {
        // inject Spring bean vào servlet thông qua ApplicationContext
        org.springframework.web.context.WebApplicationContext context =
                org.springframework.web.context.support.WebApplicationContextUtils
                        .getWebApplicationContext((ServletContext) getServletContext());
        logService = context.getBean("logServiceImp", ILogService.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String key = req.getParameter("key");
        String newPassword = req.getParameter("newPassword");
        String retypePassword = req.getParameter("retypePassword");

        String newPasswordInputErr = null;
        String retypePasswordInputErr = null;
        String newPasswordSpaceErr = null;
        String retypePasswordSpaceErr = null;
        String linkError = null;

        boolean isValid = true;

        if (!linkVerifyService.isBlankInput(newPassword) || !linkVerifyService.isBlankInput(retypePassword)) {
            if (!linkVerifyService.containsSpace(newPassword) || !linkVerifyService.containsSpace(retypePassword)) {
                if (linkVerifyService.isLengthEnough(newPassword)) {
                    if (!newPassword.equals(retypePassword)) {
                        newPasswordInputErr = "Mật khẩu và Nhập lại mật khẩu không đúng!";
                        retypePasswordInputErr = "Mật khẩu và Nhập lại mật khẩu không đúng!";
                        req.setAttribute("newPasswordInputErr", newPasswordInputErr);
                        req.setAttribute("retypePasswordInputErr", retypePasswordInputErr);
                        isValid = false;
                    }
                } else {
                    newPasswordInputErr = "Mật khẩu chứa ít nhất 6 ký tự!";
                    req.setAttribute("newPasswordInputErr", newPasswordInputErr);
                }
            } else {
                if (linkVerifyService.containsSpace(newPassword)) {
                    newPasswordSpaceErr = "Mật khẩu không được chứa ô trống!";
                    req.setAttribute("newPasswordSpaceErr", newPasswordSpaceErr);
                }
                if (linkVerifyService.containsSpace(retypePassword)) {
                    retypePasswordSpaceErr = "Mật khẩu không được chứa ô trống!";
                    req.setAttribute("retypePasswordSpaceErr", retypePasswordSpaceErr);
                }
                isValid = false;
            }
        } else {
            if (linkVerifyService.isBlankInput(newPassword)) {
                newPasswordInputErr = "Mật khẩu không được để trống!";
                req.setAttribute("newPasswordInputErr", newPasswordInputErr);
            }
            if (linkVerifyService.isBlankInput(retypePassword)) {
                retypePasswordInputErr = "Mật khẩu không được để trống!";
                req.setAttribute("retypePasswordInputErr", retypePasswordInputErr);
            }
            isValid = false;
        }

        if (!isValid) {
            req.getRequestDispatcher("change-password.jsp").forward(req, resp);
        } else {
            if (key == null || key.isEmpty() || !linkVerifyService.isCorrectKey(email, key)) {
                linkError = "e";
                req.setAttribute("linkError", linkError);
                req.getRequestDispatcher("change-password.jsp").forward(req, resp);
            } else {
                UserDTO prevUser = linkVerifyService.findUserByEmail(email);
                int affectedRows = linkVerifyService.saveRenewPasswordByEmail(email, newPassword);
                UserDTO currentUser = linkVerifyService.findUserByEmail(email);

                if (affectedRows <= 0) {
                    logService.log(req, "change-password-by-forget", LogState.FAIL, LogLevel.ALERT, prevUser, currentUser);
                    req.setAttribute("error", "e");
                    req.getRequestDispatcher("/change-password.jsp").forward(req, resp);
                } else {
                    logService.log(req, "change-password-by-forget", LogState.SUCCESS, LogLevel.WARNING, prevUser, currentUser);
                    linkVerifyService.setEmptyKey(email);
                    resp.sendRedirect(req.getContextPath() + "/change-success.jsp");
                }
            }
        }
    }
}
