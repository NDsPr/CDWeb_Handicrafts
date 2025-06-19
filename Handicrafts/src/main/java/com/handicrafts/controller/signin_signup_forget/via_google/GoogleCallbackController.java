package com.handicrafts.controller.signin_signup_forget.via_google;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.repository.UserRepository;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/google-callback")
public class GoogleCallbackController {

    private final UserRepository userRepository;
    private final Environment environment;

    public GoogleCallbackController(UserRepository userRepository, Environment environment) {
        this.userRepository = userRepository;
        this.environment = environment;
    }

    @GetMapping
    public String handleGoogleCallback(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "code", required = false) String code,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session) throws IOException {

        if (error == null) {
            if (code != null) {
                try {
                    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

                    // Lấy đường dẫn file client secret từ Environment
                    String googleClientSecretFile = environment.getProperty(
                            "oauth2dot0.google-client-secret-file",
                            "google-client-secret.json");

                    // Đọc file client secret từ classpath resources
                    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory,
                            new InputStreamReader(new ClassPathResource(googleClientSecretFile).getInputStream()));

                    // Lấy scope từ cấu hình, mặc định là email, profile, openid
                    String scopeConfig = environment.getProperty("oauth2dot0.google-scopes", "email,profile,openid");
                    List<String> scopes = Arrays.asList(scopeConfig.split(","));

                    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
                            clientSecrets, scopes)
                            .build();

                    // Lấy redirect URI từ Environment
                    String googleRedirectUri = environment.getProperty(
                            "oauth2dot0.google-redirect-uri",
                            "http://localhost:8080/google-callback");

                    GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                            .setRedirectUri(googleRedirectUri).execute();

                    String accessTokenValue = tokenResponse.getAccessToken();
                    AccessToken accessToken = new AccessToken(accessTokenValue, null);

                    // Sử dụng UserCredentials để tạo GoogleCredentials từ AccessToken
                    GoogleCredentials credential = UserCredentials.newBuilder()
                            .setClientId(clientSecrets.getDetails().getClientId())
                            .setClientSecret(clientSecrets.getDetails().getClientSecret())
                            .setAccessToken(accessToken)
                            .build();

                    // Lấy application name từ Environment
                    String applicationName = environment.getProperty(
                            "application.name",
                            "DDD. Handicraft - Nghệ thuật mỹ nghệ");

                    // Gọi Google OAuth2 API để lấy thông tin người dùng
                    Oauth2 oauth2 = new Oauth2.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credential))
                            .setApplicationName(applicationName)
                            .build();

                    Userinfo userInfo = oauth2.userinfo().get().execute();
                    String familyName = userInfo.getFamilyName();
                    String givenName = userInfo.getGivenName();
                    String email = userInfo.getEmail();

                    UserDTO user = new UserDTO();
                    user.setEmail(email);
                    // user.setFirstName(familyName);
                    // user.setLastName(givenName);

                    // Lấy provider name từ Environment
                    String providerName = environment.getProperty("oauth2dot0.google-provider-name", "Google");

                    // Nếu email chưa xuất hiện trong database thì thêm thông tin vào database
                    if (userRepository.findByEmail(email) == null) {
                        // userRepository.createOAuth(user, providerName);
                        // Set user vào Session
                        session.setAttribute("user", userRepository.findByEmail(email));

                        // Lấy redirect URL sau khi đăng nhập thành công từ Environment
                        String successRedirectUrl = environment.getProperty(
                                "oauth2dot0.success-redirect-url",
                                "/home");
                        return "redirect:" + successRedirectUrl;
                    } else {
                        String authResult = userRepository.checkOAuthAccount(email);

                        // Lấy các URL redirect từ Environment
                        String homeRedirectUrl = environment.getProperty("oauth2dot0.home-redirect-url", "/home");
                        String registeredByPageUrl = environment.getProperty(
                                "oauth2dot0.registered-by-page-url",
                                "/signin?notify=registed-by-page");
                        String errorOAuthUrl = environment.getProperty(
                                "oauth2dot0.error-oauth-url",
                                "/signin?notify=error-oauth");
                        String defaultSigninUrl = environment.getProperty(
                                "oauth2dot0.default-signin-url",
                                "/signin");

                        switch (authResult) {
                            case "oAuth":
                                // Set user vào Session
                                session.setAttribute("user", userRepository.findByEmail(email));
                                return "redirect:" + homeRedirectUrl;

                            case "notOAuth":
                                // Case này bắt null từ db, đã được xử lý trong Repository
                                return "redirect:" + registeredByPageUrl;

                            case "error":
                                return "redirect:" + errorOAuthUrl;

                            default:
                                return "redirect:" + defaultSigninUrl;
                        }
                    }

                } catch (GeneralSecurityException e) {
                    // Lấy URL lỗi từ Environment
                    String errorUrl = environment.getProperty(
                            "oauth2dot0.security-error-url",
                            "/signin?notify=security-error");
                    return "redirect:" + errorUrl;
                }
            }
        } else if (error.equals("access_denied")) {
            // Lấy URL từ chối truy cập từ Environment
            String accessDeniedUrl = environment.getProperty(
                    "oauth2dot0.access-denied-url",
                    "/signin?notify=access-denied");
            return "redirect:" + accessDeniedUrl;
        }

        // Lấy URL mặc định từ Environment
        String defaultUrl = environment.getProperty("oauth2dot0.default-url", "/signin");
        return "redirect:" + defaultUrl;
    }
}
