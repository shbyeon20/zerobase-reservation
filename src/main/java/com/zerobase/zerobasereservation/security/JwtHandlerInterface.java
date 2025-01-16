package com.zerobase.zerobasereservation.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

public interface JwtHandlerInterface {

    String generateToken(String userId, Collection<? extends GrantedAuthority> role);
    boolean validateToken(String token);
    String getMemberIdFromToken(String token);
    Collection<GrantedAuthority> getAuthoritiesFromToken(String token);

}


