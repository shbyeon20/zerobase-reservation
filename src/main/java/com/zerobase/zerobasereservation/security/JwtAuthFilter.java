package com.zerobase.zerobasereservation.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtHandler jwtHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /*
     request header 에 authentication 을 parsing 함. parsing 후 jwt 라이브러리를 통해
     claim 파싱하여 token 의 유효성검사를 실행. 유효하다면 SecurityContext 에 authentication
     생성
     */
        String REQUEST_HEADER = "Authorization";
        String authorization = request.getHeader(REQUEST_HEADER);
        String token="";

        String REQUEST_PREFIX = "Bearer ";
        if (ObjectUtils.isEmpty(authorization) && authorization.startsWith(REQUEST_PREFIX)) {
            token = authorization.substring(REQUEST_PREFIX.length());
        }
        if(!ObjectUtils.isEmpty(token)&jwtHandler.validateToken(token)){
            Authentication authentication = jwtHandler.getJwtAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }
}
