package com.handicrafts.service.impl;

import com.handicrafts.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final Environment environment;

    public JwtService(Environment environment) {
        this.environment = environment;
    }

    /**
     * Tạo JWT token từ thông tin người dùng
     */
    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roleId", user.getRoleId());

        // Thêm các claim tùy chọn từ cấu hình
        String additionalClaims = environment.getProperty("jwt.additional-claims");
        if (additionalClaims != null && !additionalClaims.isEmpty()) {
            for (String claim : additionalClaims.split(",")) {
                String[] keyValue = claim.split(":");
                if (keyValue.length == 2) {
                    claims.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }

        long jwtExpiration = Long.parseLong(
                environment.getProperty("jwt.expiration", "86400000") // Mặc định 24 giờ
        );

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Lấy khóa ký JWT
     */
    private Key getSigningKey() {
        String secretKey = environment.getProperty("jwt.secret-key");
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("JWT secret key is not configured");
        }
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Trích xuất thông tin từ token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Trích xuất tất cả thông tin từ token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Trích xuất email từ token
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Kiểm tra token có hết hạn chưa
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Trích xuất thời gian hết hạn từ token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Xác thực token
     */
    public boolean validateToken(String token, String userEmail) {
        final String email = extractEmail(token);
        return (email.equals(userEmail) && !isTokenExpired(token));
    }

    /**
     * Tạo token làm mới
     */
    public String generateRefreshToken(UserEntity user) {
        long refreshTokenExpiration = Long.parseLong(
                environment.getProperty("jwt.refresh-token.expiration", "604800000") // Mặc định 7 ngày
        );

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
