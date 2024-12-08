package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.security.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsImpl userDetailsImple;
    private final JwtHandler jwtHandler;

    public String jwtSignIn(String id, String password) {

        UserDetails userDetails = userDetailsImple
            .authenticate(id,password);

        return jwtHandler
            .generateToken(userDetails.getUsername(), userDetails.getAuthorities());
    }

}
