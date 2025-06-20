package com.handicrafts.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import com.handicrafts.oauth2.CustomOAuth2User;
import com.handicrafts.service.IUserService;
import com.handicrafts.service.impl.CustomOAuth2UserServiceImp;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private IUserService userDetailsService;

    @Autowired
    private CustomOAuth2UserServiceImp oAuth2UserService;

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain");

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authz) ->
                        authz
                                // Allow all static resources without authentication
                                .requestMatchers("/", "/home", "/dang-nhap", "/dang-ky").permitAll()
                                // Cho phép truy cập các trang đăng ký và xác thực
                                .requestMatchers("/register", "/code-verify", "/code-verification", "/verification", "/web/code-verify.html", "/web/verify-success.html").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/client/css/**").permitAll()
                                .requestMatchers("/login-signup-forget/**").permitAll() // Cho phép truy cập thư mục css/js của form đăng ký
                                // Allow specific API endpoints
                                .requestMatchers("/api/auth/**").permitAll()
                                // Protected paths that require authentication
                                .requestMatchers("/thanh-toan", "/gio-hang").authenticated()
                                // Admin-only paths
                                .requestMatchers("/admin-page/**").hasRole("ADMIN")
                                // Default rule for other requests
                                .anyRequest().permitAll())
                //login and logout
                .formLogin((formLogin) ->
                        formLogin
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .loginPage("/dang-nhap")
                                .failureUrl("/dang-nhap?error=true")
                                .defaultSuccessUrl("/", true)
                                .loginProcessingUrl("/login")
                )
                .logout((logout) ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/"))
                //login with gg
                .oauth2Login(oauth ->
                        oauth
                                .loginPage("/dang-nhap")
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig.userService(oAuth2UserService))
                                .successHandler(new AuthenticationSuccessHandler() {
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                                        String provider = oauthUser.getClientName();
                                        try {
                                            userService.processOAuthPostLogin(oauthUser.getAttribute("email"), oauthUser.getName(), provider);
                                            logger.info("OAuth login successful for user: {}", (Object) oauthUser.getAttribute("email"));
                                            response.sendRedirect("/");
                                        } catch (Exception e) {
                                            logger.error("Error during OAuth post-login processing: {}", e.getMessage(), e);
                                            response.sendRedirect("/dang-nhap?error=oauth");
                                        }

                                    }
                                }));

        http.authenticationProvider(authProvider());

        logger.info("Security filter chain configured successfully");
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        logger.info("Configuring authentication provider");

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        logger.info("Authentication provider configured successfully");
        return authProvider;
    }
}
