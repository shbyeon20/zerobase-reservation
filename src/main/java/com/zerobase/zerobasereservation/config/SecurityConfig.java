package com.zerobase.zerobasereservation.config;

import com.zerobase.zerobasereservation.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    /*
    * Jwt Statless 인증을 적용함.
    * Url base로 권한을 제한함 ;  auth : signin - signup 관련 url
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 로그인, 로그아웃
                .authorizeHttpRequests(auth -> auth.requestMatchers("/css/**"
                        ,"/js/**", "/images","/h2-console/**", "/fonts/**",
                        "/images/**", "/favicon.ico").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                       "/auth/**").permitAll())
                // url 기반으로 partner 와 user 권한부여
                .authorizeHttpRequests(auth->auth.requestMatchers(
                        "/*/partner/**").hasRole("PARTNER"))
                .authorizeHttpRequests(auth->auth.requestMatchers(
                        "/*/user/**").hasRole("USER"))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);

        log.info("Security filter chain is being configured");

        return http.build();
    }





}

