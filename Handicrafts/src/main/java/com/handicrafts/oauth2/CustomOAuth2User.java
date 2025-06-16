package com.handicrafts.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private OAuth2User oAuth2User;
    private String clientName;

    public CustomOAuth2User(OAuth2User oAuth2User, String clientName) {
        this.oAuth2User = oAuth2User;
        this.clientName = clientName;
    }

    public CustomOAuth2User() {

    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    //tra ve username cua nguoi da dang nhap
    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getClientName() {
        return this.clientName;
    }

    // Thêm phương thức getEmail()
    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }
}
