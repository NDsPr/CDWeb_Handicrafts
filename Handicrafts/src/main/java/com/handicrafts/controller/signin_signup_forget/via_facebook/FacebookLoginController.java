package com.handicrafts.controller.signin_signup_forget.via_facebook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.service.JwtService;
import com.handicrafts.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/facebook")
public class FacebookLoginController {

    @Value("${oauth.facebook.client-id}")
    private String facebookAppId;

    @Value("${oauth.facebook.client-secret}")
    private String facebookAppSecret;

    @Value("${oauth.facebook.redirect-uri}")
    private String redirectUri;

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;

    public FacebookLoginController(
            UserRepository userRepository,
            UserService userService,
            JwtService jwtService,
            RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
    }

    /**
     * Trả về URL để chuyển hướng người dùng đến trang đăng nhập Facebook
     */
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> getLoginUrl() {
        String facebookAuthUrl = "https://www.facebook.com/v18.0/dialog/oauth" +
                "?client_id=" + facebookAppId +
                "&redirect_uri=" + redirectUri +
                "&scope=email,public_profile";

        Map<String, String> response = new HashMap<>();
        response.put("url", facebookAuthUrl);

        return ResponseEntity.ok(response);
    }

    /**
     * Xử lý callback từ Facebook sau khi người dùng đăng nhập thành công
     */
    @GetMapping("/callback")
    public ResponseEntity<?> handleCallback(@RequestParam("code") String code) {
        try {
            // Bước 1: Đổi code lấy access token
            String tokenUrl = "https://graph.facebook.com/v18.0/oauth/access_token" +
                    "?client_id=" + facebookAppId +
                    "&client_secret=" + facebookAppSecret +
                    "&redirect_uri=" + redirectUri +
                    "&code=" + code;

            ResponseEntity<String> tokenResponse = restTemplate.getForEntity(tokenUrl, String.class);

            if (tokenResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Failed to get access token from Facebook");
            }

            // Parse JSON response để lấy access token
            ObjectMapper mapper = new ObjectMapper();
            JsonNode tokenNode = mapper.readTree(tokenResponse.getBody());
            String accessToken = tokenNode.get("access_token").asText();

            // Bước 2: Lấy thông tin người dùng từ Facebook
            String userInfoUrl = "https://graph.facebook.com/v18.0/me" +
                    "?fields=id,email,first_name,last_name" +
                    "&access_token=" + accessToken;

            ResponseEntity<String> userInfoResponse = restTemplate.getForEntity(userInfoUrl, String.class);

            if (userInfoResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Failed to get user info from Facebook");
            }

            // Parse thông tin người dùng
            JsonNode userInfo = mapper.readTree(userInfoResponse.getBody());
            String facebookId = userInfo.get("id").asText();
            String email = userInfo.has("email") ? userInfo.get("email").asText() : facebookId + "@facebook.com";
            String firstName = userInfo.get("first_name").asText();
            String lastName = userInfo.get("last_name").asText();

            // Bước 3: Kiểm tra xem người dùng đã tồn tại trong hệ thống chưa
            Optional<UserEntity> existingUser = userRepository.findByEmail(email);
            UserEntity user;

            if (existingUser.isPresent()) {
                // Người dùng đã tồn tại, cập nhật thông tin nếu cần
                user = existingUser.get();

                // Nếu tài khoản được tạo thông qua đăng ký thông thường, cập nhật thành OAuth
                if (user.getViaOAuth() == 0) {
                    user.setViaOAuth(1); // 1 = Facebook
                    user.setModifiedDate(Timestamp.from(Instant.now()));
                    user.setModifiedBy("FACEBOOK_AUTH");
                    userRepository.save(user);
                }
            } else {
                // Người dùng chưa tồn tại, tạo mới
                user = new UserEntity();
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPassword(""); // Không cần mật khẩu cho đăng nhập OAuth
                user.setRoleId(2); // Giả sử 2 là role "USER"
                user.setStatus(1); // Giả sử 1 là "ACTIVE"
                user.setViaOAuth(1); // 1 = Facebook
                user.setKey(facebookId); // Lưu Facebook ID vào trường key
                user.setCreatedDate(Timestamp.from(Instant.now()));
                user.setCreatedBy("FACEBOOK_AUTH");

                userRepository.save(user);
            }

            // Bước 4: Tạo JWT token
            String token = jwtService.generateToken(user);

            // Bước 5: Trả về thông tin người dùng và token
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);

            UserDTO userDTO = userService.convertToDTO(user);
            // Không trả về mật khẩu
            userDTO.setPassword(null);

            responseData.put("user", userDTO);

            return ResponseEntity.ok(responseData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during Facebook authentication: " + e.getMessage());
        }
    }
}
