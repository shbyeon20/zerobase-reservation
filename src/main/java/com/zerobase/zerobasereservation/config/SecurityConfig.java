package com.zerobase.zerobasereservation.config;

import com.zerobase.zerobasereservation.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
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
                // 기본적인 예외 url
                .authorizeHttpRequests(auth -> auth.requestMatchers("/css/**"
                        ,"/js/**", "/images","/h2-console/**", "/fonts/**",
                        "/images/**", "/favicon.ico").permitAll())
                // 로그인, 로그아웃
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/auth/**").permitAll())
                // url 기반으로 partner 와 user 권한부여
                .authorizeHttpRequests(auth->auth.requestMatchers(
                        "**/partner/**").hasRole("PARTNER"))
                .authorizeHttpRequests(auth->auth.requestMatchers(
                        "**/user/**").hasRole("USER"))

                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



}
