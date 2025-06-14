package com.handicrafts.controller.signin_signup_forget.via_facebook;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.handicrafts.oauth2.CustomOAuth2User;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.service.AuthenticationService;
import com.handicrafts.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
public class FacebookCallbackController {

    private final OAuth2Service facebookOAuth2Service;
    private final AuthenticationService authenticationService;

    @Autowired
    public FacebookCallbackController(OAuth2Service facebookOAuth2Service,
                                      AuthenticationService authenticationService) {
        this.facebookOAuth2Service = facebookOAuth2Service;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/facebook-callback")
    public String handleCallback(@RequestParam(value = "code", required = false) String code,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (code != null) {
            try {
                // Lấy access token
                OAuth2AccessToken accessToken = facebookOAuth2Service.getAccessToken(code);
                session.setAttribute("accessToken", accessToken);

                // Lấy thông tin người dùng
                CustomOAuth2User userInfo = facebookOAuth2Service.getUserInfo(accessToken);

                if (userInfo.getEmail() != null) {
                    // Kiểm tra email đã tồn tại chưa
                    String checkResult = authenticationService.checkOAuthAccount(userInfo.getEmail());

                    switch (checkResult) {
                        case "oAuth":
                            // Đăng nhập thành công với tài khoản OAuth
                            UserEntity user = authenticationService.processOAuthLogin(userInfo);
                            session.setAttribute("user", user);
                            return "redirect:/home";

                        case "notOAuth":
                            // Email đã được đăng ký bằng phương thức thông thường
                            redirectAttributes.addAttribute("notify", "registed-by-page");
                            return "redirect:/signin";

                        case "error":
                            // Có lỗi xảy ra
                            redirectAttributes.addAttribute("notify", "error-oauth");
                            return "redirect:/signin";

                        default:
                            // Tạo tài khoản mới
                            UserEntity newUser = authenticationService.processOAuthLogin(userInfo);
                            session.setAttribute("user", newUser);
                            return "redirect:/home";
                    }
                } else {
                    // Email không được cung cấp từ Facebook
                    redirectAttributes.addAttribute("notify", "not-contain-email");
                    return "redirect:/signin";
                }
            } catch (IOException | ExecutionException | InterruptedException e) {
                redirectAttributes.addAttribute("notify", "error-oauth");
                return "redirect:/signin";
            }
        }

        return "redirect:/signin";
    }
}
