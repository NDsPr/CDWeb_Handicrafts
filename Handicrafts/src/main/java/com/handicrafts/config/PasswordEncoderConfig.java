package com.handicrafts.config;

import org.springframework.context.annotation.DTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @DTO
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
