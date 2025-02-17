package com.zerobase.zerobasereservation.reservation.security;

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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/*
Jwt Auth에 관련한
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

        private final AuthServiceInterface authServiceInterface;
        private static final String REQUEST_HEADER_NAME = "Authorization";
        private static final String REQUEST_HEADER_CONTENT_PREFIX = "Bearer ";




    /*
    request header 에 authentication 을 parsing 함. parsing 후 jwt 라이브러리를 통해
    claim 파싱하여 token의 유효성검사를 실행. 유효하다면 SecurityContext에 authentication생성
    */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeaderToken = request.getHeader(REQUEST_HEADER_NAME);
        String token =null;
        log.info("JWT Filter started with request headers : "+ request.getHeaderNames());
        log.info("awtAuthFilter : authToken filter started with token : "+authHeaderToken);

        // header에서 token을 parsing한다
        if (StringUtils.hasText(authHeaderToken) && authHeaderToken.startsWith(REQUEST_HEADER_CONTENT_PREFIX)) {
            token = authHeaderToken.substring(REQUEST_HEADER_CONTENT_PREFIX.length());
            log.info("awtAuthFilter : token parsed : "+ token);
        }

        // token이 유효한지 검증한 후, token으로부터 auehtntication을 생성한다.
        if((!ObjectUtils.isEmpty(token)&&authServiceInterface.validateKey(token))){
            Authentication authentication = authServiceInterface.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("JwtAuthFilter: SecurityContextHolder set with Authentication");
        }
        log.info("authToken filter finished");
        filterChain.doFilter(request, response);
    }

    /*
       jwt토큰으로부터 sub를 모으고 spring context 에 담을 Authentication 을 생성함
    */


}
