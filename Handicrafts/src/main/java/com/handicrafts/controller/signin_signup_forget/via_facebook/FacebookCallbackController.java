package com.handicrafts.controller.signin_signup_forget.via_facebook;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.handicrafts.oauth2.CustomOAuth2User;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.service.AuthenticationService;
import com.handicrafts.service.OAuth2Service;
import org.springframework.core.env.Environment;
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
    private final Environment environment;

    public FacebookCallbackController(OAuth2Service facebookOAuth2Service,
                                      AuthenticationService authenticationService,
                                      Environment environment) {
        this.facebookOAuth2Service = facebookOAuth2Service;
        this.authenticationService = authenticationService;
        this.environment = environment;
    }

    @GetMapping({"/facebook-callback", "/fb-callback"})
    public String handleCallback(@RequestParam(value = "code", required = false) String code,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (code != null) {
            try {
                // Lấy access token
                OAuth2AccessToken accessToken = facebookOAuth2Service.getAccessToken(code);
                session.setAttribute(environment.getProperty("session.attribute.access-token", "accessToken"), accessToken);

                // Lấy thông tin người dùng
                CustomOAuth2User userInfo = facebookOAuth2Service.getUserInfo(accessToken);

                if (userInfo.getEmail() != null) {
                    // Kiểm tra email đã tồn tại chưa
                    String checkResult = authenticationService.checkOAuthAccount(userInfo.getEmail());

                    switch (checkResult) {
                        case "oAuth":
                            // Đăng nhập thành công với tài khoản OAuth
                            UserEntity user = authenticationService.processOAuthLogin(userInfo);
                            session.setAttribute(environment.getProperty("session.attribute.user", "user"), user);
                            return "redirect:" + environment.getProperty("redirect.home", "/web/home");

                        case "notOAuth":
                            // Email đã được đăng ký bằng phương thức thông thường
                            redirectAttributes.addAttribute(
                                    environment.getProperty("redirect.attribute.notify", "notify"),
                                    environment.getProperty("notify.registed-by-page", "registed-by-page")
                            );
                            return "redirect:" + environment.getProperty("redirect.signin", "/web/signin");

                        case "error":
                            // Có lỗi xảy ra
                            redirectAttributes.addAttribute(
                                    environment.getProperty("redirect.attribute.notify", "notify"),
                                    environment.getProperty("notify.error-oauth", "error-oauth")
                            );
                            return "redirect:" + environment.getProperty("redirect.signin", "/web/signin");

                        default:
                            // Tạo tài khoản mới
                            UserEntity newUser = authenticationService.processOAuthLogin(userInfo);
                            session.setAttribute(environment.getProperty("session.attribute.user", "user"), newUser);
                            return "redirect:" + environment.getProperty("redirect.home", "/web/home");
                    }
                } else {
                    // Email không được cung cấp từ Facebook
                    redirectAttributes.addAttribute(
                            environment.getProperty("redirect.attribute.notify", "notify"),
                            environment.getProperty("notify.not-contain-email", "not-contain-email")
                    );
                    return "redirect:" + environment.getProperty("redirect.signin", "/web/signin");
                }
            } catch (IOException | ExecutionException | InterruptedException e) {
                redirectAttributes.addAttribute(
                        environment.getProperty("redirect.attribute.notify", "notify"),
                        environment.getProperty("notify.error-oauth", "error-oauth")
                );
                return "redirect:" + environment.getProperty("redirect.signin", "/web/signin");
            }
        }

        return "redirect:" + environment.getProperty("redirect.signin", "/web/signin");
    }
}
