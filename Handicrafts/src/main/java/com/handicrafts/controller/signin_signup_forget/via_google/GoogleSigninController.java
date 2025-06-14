package com.handicrafts.controller.signin_signup_forget.via_google;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

// Servlet nhận request login via Google và chuyển hướng về Google
@WebServlet(value = {"/signin-via-google"})
public class GoogleSigninController extends HttpServlet {
    private static ResourceBundle bundle = ResourceBundle.getBundle("oauth2dot0");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientId = bundle.getString("google-client-id");
        String redirectUri = bundle.getString("google-redirect-uri");
        String googleLoginUrl = "https://accounts.google.com/o/oauth2/auth?client_id=" + clientId
                + "&response_type=code&redirect_uri=" + redirectUri + "&scope=email%20profile&access_type=offline";

        resp.sendRedirect(googleLoginUrl);
    }
}
