package com.zerobase.zerobasereservation.security;

import com.zerobase.zerobasereservation.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceJwtImpl implements AuthServiceInterface {


    private final UserDetailsImpl userDetailsImpl;
    private final JwtHandler jwtHandler;
    private final JwtHandlerInterface jwtHandlerInterface;

    @Override
    public Authentication getAuthentication(String jwtToken) {

        log.info("creat authentication through token : " + jwtToken);
        UserDetails userDetails = userDetailsImpl.loadUserByUsername(jwtHandlerInterface.getMemberIdFromToken(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    @Override
    public String getKey(String id, String password) {

        UserDetails userDetails = userDetailsImpl.authenticate(id, password);
        return jwtHandlerInterface.generateToken(userDetails.getUsername(),
            userDetails.getAuthorities());
    }

    @Override
    public Boolean validateKey(String secret) {
        return jwtHandlerInterface.validateToken(secret);
    }


}
