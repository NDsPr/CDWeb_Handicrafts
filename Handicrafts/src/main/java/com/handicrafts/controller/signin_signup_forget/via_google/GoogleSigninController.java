package com.handicrafts.controller.signin_signup_forget.via_google;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Controller nhận request login via Google và chuyển hướng về Google
 */
@Controller
@RequestMapping("/signin-via-google")
public class GoogleSigninController {

    private final Environment environment;

    public GoogleSigninController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping
    public void signinViaGoogle(HttpServletResponse response) throws IOException {
        // Lấy client ID từ Environment
        String googleClientId = environment.getProperty(
                "oauth2dot0.google-client-id",
                "default-client-id");

        // Lấy redirect URI từ Environment
        String googleRedirectUri = environment.getProperty(
                "oauth2dot0.google-redirect-uri",
                "http://localhost:8080/google-callback");

        // Lấy response type từ Environment hoặc sử dụng giá trị mặc định "code"
        String responseType = environment.getProperty(
                "oauth2dot0.response-type",
                "code");

        // Lấy scopes từ Environment hoặc sử dụng giá trị mặc định "email profile"
        String scopesConfig = environment.getProperty(
                "oauth2dot0.google-scopes",
                "email,profile");

        // Chuyển đổi danh sách scopes thành chuỗi được mã hóa URL
        String scopes = Arrays.stream(scopesConfig.split(","))
                .map(String::trim)
                .collect(Collectors.joining(" "));
        String encodedScopes = URLEncoder.encode(scopes, StandardCharsets.UTF_8.toString());

        // Lấy access type từ Environment hoặc sử dụng giá trị mặc định "offline"
        String accessType = environment.getProperty(
                "oauth2dot0.access-type",
                "offline");

        // Lấy URL cơ sở của Google OAuth từ Environment hoặc sử dụng giá trị mặc định
        String googleOAuthBaseUrl = environment.getProperty(
                "oauth2dot0.google-auth-url",
                "https://accounts.google.com/o/oauth2/auth");

        // Xây dựng URL đầy đủ
        StringBuilder googleLoginUrlBuilder = new StringBuilder(googleOAuthBaseUrl);
        googleLoginUrlBuilder.append("?client_id=").append(googleClientId);
        googleLoginUrlBuilder.append("&response_type=").append(responseType);
        googleLoginUrlBuilder.append("&redirect_uri=").append(URLEncoder.encode(googleRedirectUri, StandardCharsets.UTF_8.toString()));
        googleLoginUrlBuilder.append("&scope=").append(encodedScopes);
        googleLoginUrlBuilder.append("&access_type=").append(accessType);

        // Thêm các tham số tùy chọn từ cấu hình (nếu có)
        String optionalParams = environment.getProperty("oauth2dot0.google-optional-params");
        if (optionalParams != null && !optionalParams.isEmpty()) {
            googleLoginUrlBuilder.append("&").append(optionalParams);
        }

        // Chuyển hướng đến URL đăng nhập Google
        response.sendRedirect(googleLoginUrlBuilder.toString());
    }
}
