package com.handicrafts.controller.signin_signup_forget.via_facebook;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet(value = "/signin-via-facebook")
public class FacebookSigninController extends HttpServlet {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("oauth2dot0");
    private static final OAuth20Service service = new ServiceBuilder(bundle.getString("facebook-client-id"))
            .apiSecret(bundle.getString("facebook-client-secret"))
            .callback(bundle.getString("facebook-redirect-uri"))
            .defaultScope("email,public_profile") // specify the scope if needed
            .build(FacebookApi.instance());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authorizationUrl = service.getAuthorizationUrl();
        resp.sendRedirect(authorizationUrl);
    }
}
