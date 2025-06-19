package com.handicrafts.controller.signin_signup_forget.via_facebook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.RoleEntity;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.RoleRepository;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.service.impl.JwtService;
import com.handicrafts.service.IUserService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api/auth/facebook")
public class FacebookLoginController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final IUserService userService;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;
    private final Environment environment;

    public FacebookLoginController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            IUserService userService,
            JwtService jwtService,
            Environment environment) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.restTemplate = new RestTemplate();
        this.environment = environment;
    }

    /**
     * Trả về URL để chuyển hướng người dùng đến trang đăng nhập Facebook
     */
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> getLoginUrl() {
        String facebookAppId = environment.getProperty("oauth.facebook.client-id");
        String redirectUri = environment.getProperty("oauth.facebook.redirect-uri");
        String scope = environment.getProperty("oauth.facebook.scope", "email,public_profile");
        String facebookApiVersion = environment.getProperty("oauth.facebook.api-version", "v18.0");

        String facebookAuthUrl = "https://www.facebook.com/" + facebookApiVersion + "/dialog/oauth" +
                "?client_id=" + facebookAppId +
                "&redirect_uri=" + redirectUri +
                "&scope=" + scope;

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
            String facebookAppId = environment.getProperty("oauth.facebook.client-id");
            String facebookAppSecret = environment.getProperty("oauth.facebook.client-secret");
            String redirectUri = environment.getProperty("oauth.facebook.redirect-uri");
            String facebookApiVersion = environment.getProperty("oauth.facebook.api-version", "v18.0");

            // Bước 1: Đổi code lấy access token
            String tokenUrl = "https://graph.facebook.com/" + facebookApiVersion + "/oauth/access_token" +
                    "?client_id=" + facebookAppId +
                    "&client_secret=" + facebookAppSecret +
                    "&redirect_uri=" + redirectUri +
                    "&code=" + code;

            ResponseEntity<String> tokenResponse = restTemplate.getForEntity(tokenUrl, String.class);

            if (tokenResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(environment.getProperty("oauth.facebook.error.token", "Failed to get access token from Facebook"));
            }

            // Parse JSON response để lấy access token
            ObjectMapper mapper = new ObjectMapper();
            JsonNode tokenNode = mapper.readTree(tokenResponse.getBody());
            String accessToken = tokenNode.get("access_token").asText();

            // Bước 2: Lấy thông tin người dùng từ Facebook
            String userFields = environment.getProperty("oauth.facebook.user-fields", "id,email,name");
            String userInfoUrl = "https://graph.facebook.com/" + facebookApiVersion + "/me" +
                    "?fields=" + userFields +
                    "&access_token=" + accessToken;

            ResponseEntity<String> userInfoResponse = restTemplate.getForEntity(userInfoUrl, String.class);

            if (userInfoResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(environment.getProperty("oauth.facebook.error.user-info", "Failed to get user info from Facebook"));
            }

            // Parse thông tin người dùng
            JsonNode userInfo = mapper.readTree(userInfoResponse.getBody());
            String facebookId = userInfo.get("id").asText();
            String email = userInfo.has("email") ? userInfo.get("email").asText() : facebookId + "@facebook.com";
            String fullname = userInfo.get("name").asText();

            // Bước 3: Kiểm tra xem người dùng đã tồn tại trong hệ thống chưa
            UserEntity existingUser = userRepository.findByEmail(email);
            UserEntity user;

            if (existingUser != null) {
                // Người dùng đã tồn tại, cập nhật thông tin nếu cần
                user = existingUser;

                // Nếu tài khoản chưa được đánh dấu là từ provider Facebook
                String oauthProvider = environment.getProperty("oauth.facebook.provider-name", "facebook");
                if (user.getViaOAuth() == null || !user.getViaOAuth().equals(oauthProvider)) {
                    user.setViaOAuth(oauthProvider);
                    user.setModifiedDate(new Timestamp(System.currentTimeMillis()));
                    userRepository.save(user);
                }
            } else {
                // Người dùng chưa tồn tại, tạo mới
                user = new UserEntity();
                user.setEmail(email);

                // Phân tách fullname thành firstName và lastName
                String[] nameParts = fullname.split(" ", 2);
                if (nameParts.length > 1) {
                    user.setFirstName(nameParts[0]);
                    user.setLastName(nameParts[1]);
                } else {
                    user.setFirstName(fullname);
                    user.setLastName("");
                }

                user.setPassword(""); // Không cần mật khẩu cho đăng nhập OAuth
                user.setViaOAuth(environment.getProperty("oauth.facebook.provider-name", "facebook")); // Đánh dấu là từ Facebook
                user.setStatus(Integer.parseInt(environment.getProperty("user.status.active", "1"))); // Giả sử 1 là trạng thái active
                user.setCreatedDate(new Timestamp(System.currentTimeMillis()));

                // Thêm role USER cho người dùng mới - sử dụng Optional
                String defaultRoleName = environment.getProperty("user.default-role", "ROLE_USER");
                Optional<RoleEntity> userRoleOptional = roleRepository.findByName(defaultRoleName);
                if (userRoleOptional.isEmpty()) {
                    throw new RuntimeException(environment.getProperty("error.role-not-found", "Default role not found"));
                }
                RoleEntity userRole = userRoleOptional.get();
                user.setRoleId(userRole.getRoleID());

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
                    .body(environment.getProperty("oauth.error.general", "Error during Facebook authentication: ") + e.getMessage());
        }
    }

    // Thêm phương thức tiện ích để kiểm tra tài khoản OAuth
    private boolean isOAuthAccount(UserEntity user) {
        String oauthCheckField = environment.getProperty("oauth.account-check-field", "addressProvince");

        try {
            // Sử dụng reflection để lấy giá trị của trường được cấu hình
            java.lang.reflect.Method getter = UserEntity.class.getMethod("get" + oauthCheckField.substring(0, 1).toUpperCase() + oauthCheckField.substring(1));
            Object value = getter.invoke(user);
            return value != null && !value.toString().isEmpty();
        } catch (Exception e) {
            // Fallback nếu không thể sử dụng reflection
            return user.getAddressProvince() != null && !user.getAddressProvince().isEmpty();
        }
    }
}
