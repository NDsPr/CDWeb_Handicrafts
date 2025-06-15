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
import com.handicrafts.bean.UserBean;
import com.handicrafts.dao.UserDAO;
import com.handicrafts.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.ResourceBundle;

@WebServlet(value = {"/google-callback"})
public class GoogleCallbackController extends HttpServlet {
    private final ResourceBundle bundle = ResourceBundle.getBundle("oauth2dot0");
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String error = req.getParameter("error");
        if (error == null) {
            String code = req.getParameter("code");
            if (code != null) {
                try {
                    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

                    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory,
                            new InputStreamReader(GoogleCallbackController.class.getResourceAsStream(bundle.getString("google-client-secret-file"))));

                    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
                            clientSecrets, Arrays.asList("email", "profile", "openid"))
                            .build();

                    GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                            .setRedirectUri(bundle.getString("google-redirect-uri")).execute();

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

                    UserBean user = new UserBean();
                    user.setEmail(email);
                    user.setFirstName(familyName);
                    user.setLastName(givenName);

                    // Nếu email chưa xuất hiện trong database thì thêm thông tin vào database
                    if (userDAO.findUserByEmail(email) == null) {
                        userDAO.createOAuth(user, "Google");
                        // Set user vào Session
                        SessionUtil.getInstance().putValue(req, "user", userDAO.findUserByEmail(email));
                        resp.sendRedirect(req.getContextPath() + "/home");
                    } else {
                        switch (userDAO.checkOAuthAccount(email)) {
                            case "oAuth":
                                // Set user vào Session
                                SessionUtil.getInstance().putValue(req, "user", userDAO.findUserByEmail(email));
                                resp.sendRedirect(req.getContextPath() + "/home");
                                break;

                            case "notOAuth":
                                // Case này bắt null từ db, đã được xử lý trong DAO
                                resp.sendRedirect(req.getContextPath() + "/signin?notify=registed-by-page");
                                break;

                            case "error":
                                resp.sendRedirect(req.getContextPath() + "/signin?notify=error-oauth");
                                break;

                            default:
                                resp.sendRedirect(req.getContextPath() + "/signin");
                                break;
                        }
                    }

                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (error.equals("access_denied")) {
            resp.sendRedirect(req.getContextPath() + "/signin?notify=access-denied");
        }
    }
}