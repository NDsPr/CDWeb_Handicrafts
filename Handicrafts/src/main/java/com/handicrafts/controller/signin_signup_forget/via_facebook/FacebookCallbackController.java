package com.handicrafts.controller.signin_signup_forget.via_facebook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.ltw.bean.UserBean;
import com.ltw.dao.UserDAO;
import com.ltw.util.SessionUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

@WebServlet(value = {"/facebook-callback"})
public class FacebookCallbackController extends HttpServlet {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("oauth2dot0");
    private final UserDAO userDAO = new UserDAO();
    private static final OAuth20Service service = new ServiceBuilder(bundle.getString("facebook-client-id"))
            .apiSecret(bundle.getString("facebook-client-secret"))
            .callback(bundle.getString("facebook-redirect-uri"))
            // Scope chỉ lấy email và public_profile
            .defaultScope("email,public_profile")
            .build(FacebookApi.instance());

    // Mặc định Facebook đăng nhập phải có email
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if (code != null) {
            try {
                OAuth2AccessToken accessToken = service.getAccessToken(code);
                req.getSession().setAttribute("accessToken", accessToken);

                // Lấy thông tin người dùng
                JsonNode userInfo = getJsonUserInfo(accessToken);
                String lastName = userInfo.get("name").asText();
                String email = userInfo.has("email") ? userInfo.get("email").asText() : null;

                if (email != null) {
                    UserBean user = new UserBean();
                    user.setEmail(email);
                    user.setLastName(lastName);

                    // Nếu email chưa xuất hiện trong database thì thêm thông tin vào database
                    if (userDAO.findUserByEmail(email) == null) {
                        userDAO.createInRegister(user);
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
                } else {
                    resp.sendRedirect(req.getContextPath() + "/signin?notify=not-contain-email");
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private JsonNode getJsonUserInfo(OAuth2AccessToken accessToken) throws IOException {
        String userInfoEndpoint = "https://graph.facebook.com/me?fields=id,name,email,birthday,picture&access_token=" + accessToken.getAccessToken();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(userInfoEndpoint);
        HttpResponse response = httpClient.execute(httpGet);
        String responseString = EntityUtils.toString(response.getEntity());

        // Parse the JSON response
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(responseString);
    }
}
