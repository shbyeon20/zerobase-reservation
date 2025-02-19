package com.zerobase.zerobasereservation.reservation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtHandlerImpl implements JwtHandlerInterface {
    private static final String KEY_ROLES = "roles";
    private static final Long TIME_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L;//1hour


    @Value("${spring.jwt.secret}")
    private String secretKey;

    /*
      DAO Authentication 을 진행한 후, entity 의 username(principal)과 roles
      (authorities)를 JWT 에 담아내어 반환한다.
     */

    public String generateToken(String userId, Collection<? extends GrantedAuthority> role) {
        log.info("Generating token for user: " + userId + " with role: " + role);

        SecretKey SecretKeyObject = new SecretKeySpec(secretKey.getBytes(),
                SignatureAlgorithm.HS512.getJcaName());

        Claims customClaims = Jwts.claims();

        // 단일 권한만 저장 (첫 번째 권한 선택)
        String singleRole = role.stream()
            .map(GrantedAuthority::getAuthority) // 권한 이름만 추출
            .findFirst() // 첫 번째 권한 가져오기
            .orElse("ROLE_USER"); // 기본값 설정 (권한이 없는 경우)


        customClaims.put(KEY_ROLES,singleRole);

        return Jwts.builder()
                .setClaims(customClaims)
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+TIME_TOKEN_EXPIRE_TIME))
                .signWith( SecretKeyObject,SignatureAlgorithm.HS512)
                .compact();
    }



    private Claims parseClaimsFromToken(String token) {

        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());


        try {
            return Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    /*
        토큰이 비어있는지 혹은 토큰이 유효기간에 부합하는지 확인
     */
    public boolean validateToken(String token) {
        if(!StringUtils.hasText(token)) return false;

        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());

        log.info("Checking if token is valid: " + token);
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        Claims claims = this.parseClaimsFromToken(token);
        return claims.getExpiration().after(new Date());
    }


    public String getMemberIdFromToken(String jwt) {
        Claims claims = this.parseClaimsFromToken(jwt);
        return claims.getSubject();
    }

    @Override
    public Collection<GrantedAuthority> getAuthoritiesFromToken(String jwt) {
        Claims claims = this.parseClaimsFromToken(jwt);

        // JWT에서 단일 역할(Role) 가져오기
        String role = claims.get(KEY_ROLES, String.class);

        // 역할이 없을 경우 기본값 설정 (선택 사항)
        if (role == null || role.isBlank()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }

        // 단일 권한을 Collection으로 변환
        return List.of((GrantedAuthority) () -> role);
    }

}

