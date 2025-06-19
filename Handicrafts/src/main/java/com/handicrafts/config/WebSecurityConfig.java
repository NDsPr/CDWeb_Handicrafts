package com.handicrafts.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    @Autowired
    private IUserService userDetailsService;
    @Autowired
    private CustomOAuth2UserServiceImp oAuth2UserService;
    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Tiêm trực tiếp PasswordEncoder

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authz) ->
                        authz.requestMatchers("/", "/home", "/css/**", "/js/**", "/images/**", "/api/auth/**").permitAll()
                                .requestMatchers("/thanh-toan", "/gio-hang").authenticated()
                                .requestMatchers("/admin-page/**").hasRole("ADMIN")
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
                                        userService.processOAuthPostLogin(oauthUser.getAttribute("email"), oauthUser.getName(), provider);
                                        response.sendRedirect("/");
                                    }
                                }));
        http.authenticationProvider(authProvider());
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder); // Sử dụng trực tiếp PasswordEncoder
        return authProvider;
    }
}
