package com.handicrafts.controller.signin_signup_forget.via_google;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller nhận request login via Google và chuyển hướng về Google
 */
@Controller
@RequestMapping("/signin-via-google")
public class GoogleSigninController {

    @Value("${oauth2dot0.google-client-id}")
    private String googleClientId;

    @Value("${oauth2dot0.google-redirect-uri}")
    private String googleRedirectUri;

    @GetMapping
    public void signinViaGoogle(HttpServletResponse response) throws IOException {
        String googleLoginUrl = "https://accounts.google.com/o/oauth2/auth?client_id=" + googleClientId
                + "&response_type=code&redirect_uri=" + googleRedirectUri
                + "&scope=email%20profile&access_type=offline";

        response.sendRedirect(googleLoginUrl);
    }
}
