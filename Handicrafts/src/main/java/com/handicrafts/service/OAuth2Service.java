package com.handicrafts.service;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.handicrafts.oauth2.CustomOAuth2User;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface OAuth2Service {
    String getAuthorizationUrl();
    OAuth2AccessToken getAccessToken(String code) throws IOException, ExecutionException, InterruptedException;
    CustomOAuth2User getUserInfo(OAuth2AccessToken accessToken) throws IOException;
}
