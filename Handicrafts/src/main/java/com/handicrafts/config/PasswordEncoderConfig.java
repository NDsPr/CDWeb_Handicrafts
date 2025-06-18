package com.handicrafts.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
