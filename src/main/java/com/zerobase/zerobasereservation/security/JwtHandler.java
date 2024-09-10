package com.zerobase.zerobasereservation.security;

import com.zerobase.zerobasereservation.type.ROLE;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtHandler {
    private static final String KEY_ROLES = "roles";
    private static final Long TIME_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L;//1hour
    private final MemberAuthService memberAuthService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    /*
      DAO Authentication 을 진행한 후, entity 의 username(principal)과 roles
      (authorities)를 JWT 에 담아내어 반환한다.
     */

    public String generateToken(String username, ROLE role) {
        log.info("Generating token for user: " + username + " with role: " + role);

        SecretKey SecretKeyObject = new SecretKeySpec(secretKey.getBytes(),
                SignatureAlgorithm.HS512.getJcaName());

        Claims customClaims = Jwts.claims();
        customClaims.put("ROLE",role);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+TIME_TOKEN_EXPIRE_TIME))
                .signWith( SecretKeyObject,SignatureAlgorithm.HS512)
                .setClaims(customClaims)
                .compact();
    }


    public String getUsernameFromToken(String token) {
        log.info("get username from token : " + token);
        Claims claims = this.parseClaimsFromToken(token);
        return claims.getSubject();
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
        return claims.getExpiration().before(new Date());
    }


    /*
       spring context 에 담을 Authentication 을 생성함
    */

    public Authentication getJwtAuthentication(String jwt) {
        log.info("creat authentication through token : " + jwt);
        UserDetails userDetails =
                memberAuthService.loadUserByUsername(this.getUsernameFromToken(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails,"",
                userDetails.getAuthorities());
    }






}

