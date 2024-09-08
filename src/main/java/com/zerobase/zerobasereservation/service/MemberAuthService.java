package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.entity.MemberDetails;
import com.zerobase.zerobasereservation.exception.CustomException;
import com.zerobase.zerobasereservation.repository.MemberRepository;
import com.zerobase.zerobasereservation.security.JWTHandler;
import com.zerobase.zerobasereservation.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTHandler JWTHandler;


    public void register(String memberId, String password){
        if(memberRepository.existsByMemberId(memberId)){
            throw new UsernameNotFoundException("Member already exists");
        }

        MemberDetails.builder().memberId(memberId).password(passwordEncoder.encode(password)).build();

    }

    // daoAuth를 진행하고 JWT를 반환함
    public String daoAuth(String memberId, String password){

        MemberDetails memberDetails = memberRepository.findByMemberId(memberId).
                orElseThrow(() -> new UsernameNotFoundException("Member does not exist"));

        if(!passwordEncoder.matches(password, memberDetails.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_UNMATCHED);
        }

        String jwt = JWTHandler.generateToken(memberDetails.getMemberId(),
                memberDetails.getRole());

        return jwt;

    }




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


}
