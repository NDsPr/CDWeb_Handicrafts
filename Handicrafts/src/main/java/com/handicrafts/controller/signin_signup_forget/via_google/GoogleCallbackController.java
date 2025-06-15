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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;

@Controller
@RequestMapping("/google-callback")
public class GoogleCallbackController {

    @Value("${oauth2dot0.google-client-secret-file}")
    private String googleClientSecretFile;

    @Value("${oauth2dot0.google-redirect-uri}")
    private String googleRedirectUri;

    private final UserRepository userRepository;

    @Autowired
    public GoogleCallbackController(UserRepository userRepository) {
        this.userRepository = userRepository;
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

                    // Đọc file client secret từ classpath resources
                    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory,
                            new InputStreamReader(new ClassPathResource(googleClientSecretFile).getInputStream()));

                    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
                            clientSecrets, Arrays.asList("email", "profile", "openid"))
                            .build();

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

                    // Gọi Google OAuth2 API để lấy thông tin người dùng
                    Oauth2 oauth2 = new Oauth2.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credential))
                            .setApplicationName("DDD. Handicraft - Nghệ thuật mỹ nghệ")
                            .build();

                    Userinfo userInfo = oauth2.userinfo().get().execute();
                    String familyName = userInfo.getFamilyName();
                    String givenName = userInfo.getGivenName();
                    String email = userInfo.getEmail();

                    UserDTO user = new UserDTO();
                    user.setEmail(email);
//                    user.setFirstName(familyName);
//                    user.setLastName(givenName);

                    // Nếu email chưa xuất hiện trong database thì thêm thông tin vào database
                    if (userRepository.findByEmail(email) == null) {
//                        userRepository.createOAuth(user, "Google");
                        // Set user vào Session
                        session.setAttribute("user", userRepository.findByEmail(email));
                        return "redirect:/home";
                    } else {
                        String authResult = userRepository.checkOAuthAccount(email);
                        switch (authResult) {
                            case "oAuth":
                                // Set user vào Session
                                session.setAttribute("user", userRepository.findByEmail(email));
                                return "redirect:/home";

                            case "notOAuth":
                                // Case này bắt null từ db, đã được xử lý trong Repository
                                return "redirect:/signin?notify=registed-by-page";

                            case "error":
                                return "redirect:/signin?notify=error-oauth";

                            default:
                                return "redirect:/signin";
                        }
                    }

                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (error.equals("access_denied")) {
            return "redirect:/signin?notify=access-denied";
        }

        return "redirect:/signin";
    }
}
