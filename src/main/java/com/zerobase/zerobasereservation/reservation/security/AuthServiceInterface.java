package com.zerobase.zerobasereservation.reservation.security;

import org.springframework.security.core.Authentication;

public interface AuthServiceInterface {

    Authentication getAuthentication(String key);

    String getKey(String id, String password);
    
    Boolean validateKey(String secret);
}
