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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;
import java.util.Date;

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
    private final RoleRepository roleRepository;
    private final IUserService userService;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;

    public FacebookLoginController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            IUserService userService,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.restTemplate = new RestTemplate();
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
                    "?fields=id,email,name" +
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
            String fullname = userInfo.get("name").asText();
// Bước 3: Kiểm tra xem người dùng đã tồn tại trong hệ thống chưa
            UserEntity existingUser = userRepository.findByEmail(email);
            UserEntity user;

            if (existingUser != null) {
                // Người dùng đã tồn tại, cập nhật thông tin nếu cần
                user = existingUser;

                // Nếu tài khoản chưa được đánh dấu là từ provider Facebook
                if (user.getViaOAuth() == null || !user.getViaOAuth().equals("facebook")) {
                    user.setViaOAuth("facebook");
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
                user.setViaOAuth("facebook"); // Đánh dấu là từ Facebook
                user.setStatus(1); // Giả sử 1 là trạng thái active
                user.setCreatedDate(new Timestamp(System.currentTimeMillis()));

                // Thêm role USER cho người dùng mới - sử dụng Optional
                Optional<RoleEntity> userRoleOptional = roleRepository.findByName("ROLE_USER");
                if (userRoleOptional.isEmpty()) {
                    throw new RuntimeException("Role USER not found");
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
                    .body("Error during Facebook authentication: " + e.getMessage());
        }
    }

    // Thêm phương thức tiện ích để kiểm tra tài khoản OAuth
    private boolean isOAuthAccount(UserEntity user) {
        return user.getAddressProvince() != null && !user.getAddressProvince().isEmpty();
    }
}
