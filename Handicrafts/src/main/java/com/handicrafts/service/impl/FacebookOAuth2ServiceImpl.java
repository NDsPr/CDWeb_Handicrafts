package com.handicrafts.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.handicrafts.dto.OAuth2UserInfo;
import com.handicrafts.service.OAuth2Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class FacebookOAuth2ServiceImpl implements OAuth2Service {

    @Value("${oauth2.facebook.client-id}")
    private String clientId;

    @Value("${oauth2.facebook.client-secret}")
    private String clientSecret;

    @Value("${oauth2.facebook.redirect-uri}")
    private String redirectUri;

    private OAuth20Service createOAuth20Service() {
        return new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .callback(redirectUri)
                .defaultScope("email,public_profile")
                .build(FacebookApi.instance());
    }

    @Override
    public String getAuthorizationUrl() {
        return createOAuth20Service().getAuthorizationUrl();
    }

    @Override
    public OAuth2AccessToken getAccessToken(String code) throws IOException, ExecutionException, InterruptedException {
        return createOAuth20Service().getAccessToken(code);
    }

    @Override
    public OAuth2UserInfo getUserInfo(OAuth2AccessToken accessToken) throws IOException {
        JsonNode userInfo = getJsonUserInfo(accessToken);

        OAuth2UserInfo userInfoDto = new OAuth2UserInfo();
        userInfoDto.setName(userInfo.get("name").asText());
        userInfoDto.setEmail(userInfo.has("email") ? userInfo.get("email").asText() : null);
        userInfoDto.setProvider("facebook");
        userInfoDto.setProviderId(userInfo.get("id").asText());

        if (userInfo.has("picture") && userInfo.get("picture").has("data")) {
            userInfoDto.setPictureUrl(userInfo.get("picture").get("data").get("url").asText());
        }

        return userInfoDto;
    }

    private JsonNode getJsonUserInfo(OAuth2AccessToken accessToken) throws IOException {
        String userInfoEndpoint = "https://graph.facebook.com/me?fields=id,name,email,birthday,picture&access_token="
                + accessToken.getAccessToken();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(userInfoEndpoint);
        HttpResponse response = httpClient.execute(httpGet);
        String responseString = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(responseString);
    }
}
