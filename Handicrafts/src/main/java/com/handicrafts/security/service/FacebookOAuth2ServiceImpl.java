package com.handicrafts.security.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.handicrafts.oauth2.CustomOAuth2User;
import com.handicrafts.service.OAuth2Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FacebookOAuth2ServiceImpl implements OAuth2Service {

    private final Environment environment;

    public FacebookOAuth2ServiceImpl(Environment environment) {
        this.environment = environment;
    }

    private OAuth20Service createOAuth20Service() {
        String clientId = environment.getProperty("oauth2.facebook.client-id");
        String clientSecret = environment.getProperty("oauth2.facebook.client-secret");
        String redirectUri = environment.getProperty("oauth2.facebook.redirect-uri");
        String defaultScope = environment.getProperty("oauth2.facebook.default-scope", "email,public_profile");

        return new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .callback(redirectUri)
                .defaultScope(defaultScope)
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
    public CustomOAuth2User getUserInfo(OAuth2AccessToken accessToken) throws IOException {
        JsonNode userInfo = getJsonUserInfo(accessToken);

        // Tạo một Map<String, Object> để chứa thông tin người dùng
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(
                environment.getProperty("oauth2.user.attribute.name", "name"),
                userInfo.get("name").asText()
        );

        if (userInfo.has("email")) {
            attributes.put(
                    environment.getProperty("oauth2.user.attribute.email", "email"),
                    userInfo.get("email").asText()
            );
        }

        attributes.put(
                environment.getProperty("oauth2.user.attribute.id", "id"),
                userInfo.get("id").asText()
        );

        if (userInfo.has("picture") && userInfo.get("picture").has("data")) {
            attributes.put(
                    environment.getProperty("oauth2.user.attribute.picture", "picture"),
                    userInfo.get("picture").get("data").get("url").asText()
            );
        }

        // Tạo một OAuth2User từ attributes
        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(
                        environment.getProperty("oauth2.user.role", "ROLE_USER")
                )),
                attributes,
                environment.getProperty("oauth2.user.name-attribute-key", "name")
        );

        // Trả về CustomOAuth2User với OAuth2User và clientName
        return new CustomOAuth2User(oAuth2User,
                environment.getProperty("oauth2.client.name.facebook", "facebook")
        );
    }

    private JsonNode getJsonUserInfo(OAuth2AccessToken accessToken) throws IOException {
        String userInfoEndpointTemplate = environment.getProperty(
                "oauth2.facebook.user-info-endpoint",
                "https://graph.facebook.com/me?fields=id,name,email,birthday,picture&access_token=%s"
        );

        String userInfoEndpoint = String.format(userInfoEndpointTemplate, accessToken.getAccessToken());

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(userInfoEndpoint);
        HttpResponse response = httpClient.execute(httpGet);
        String responseString = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(responseString);
    }
}
