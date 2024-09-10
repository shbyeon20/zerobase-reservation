package com.zerobase.zerobasereservation.security;

import com.zerobase.zerobasereservation.entity.MemberDetails;
import com.zerobase.zerobasereservation.exception.CustomException;
import com.zerobase.zerobasereservation.repository.MemberRepository;
import com.zerobase.zerobasereservation.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j

public class MemberAuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHandler jwtHandler;




    /*
    유저로부터 Id와 Pw를 받아서 Id중복여부를 확인한 후
    pw를 encoding하여 db에 저장함
     */
    @Transactional
    public void register(String memberId, String password){
        if(memberRepository.existsByMemberId(memberId)){
            throw new UsernameNotFoundException("Member already exists");
        }

        MemberDetails.builder().memberId(memberId).password(passwordEncoder.encode(password)).build();
    }

    /*
       ID와 PW를 기반으로 daoAuth를 진행하고 일치한다면 JWT를 반환함
     */

    public String daoAuth(String memberId, String password){

        MemberDetails memberDetails = memberRepository.findByMemberId(memberId).
                orElseThrow(() -> new UsernameNotFoundException("Member does not exist"));

        if(!passwordEncoder.matches(password, memberDetails.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_UNMATCHED);
        }

        return jwtHandler.generateToken(memberDetails.getMemberId(),
                memberDetails.getRole());
    }






    @Override
    public  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByMemberId(username).orElseThrow(() -> new UsernameNotFoundException("Member does not exist"));
    }



}
