package com.handicrafts.service;

import com.handicrafts.oauth2.CustomOAuth2User;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.oauth2.CustomOAuth2User;
import org.springframework.transaction.annotation.Transactional;

public interface AuthenticationService {

    @Transactional
    UserEntity processOAuthLogin(CustomOAuth2User userInfo);

    String checkOAuthAccount(String email);
}
