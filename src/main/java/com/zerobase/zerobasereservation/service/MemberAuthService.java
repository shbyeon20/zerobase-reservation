package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.entity.MemberDetails;
import com.zerobase.zerobasereservation.exception.CustomException;
import com.zerobase.zerobasereservation.repository.MemberRepository;
import com.zerobase.zerobasereservation.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j

/*
유저 디테일을 불러오고 저장하는 클래스
 */
public class MemberAuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    /*
    유저로부터 Id와 Pw를 받아서 Id중복여부를 확인한 후
    pw를 encoding하여 db에 저장함
     */
    @Transactional
    public void register(String memberId, String password){
        if(memberRepository.existsByMemberId(memberId)){
            throw new CustomException(ErrorCode.MEMBERID_DUPLICATE);
        }

        memberRepository.save(
                MemberDetails.builder()
                        .memberId(memberId)
                        .password(passwordEncoder.encode(password))
                        .build());
    }

    /*
       sign-in에서 사용된 id, pw가 일치하는지 확인하고, 일치하면 userdetails반환
     */
    public UserDetails authenticate(String memberId, String password ){
        UserDetails userDetails = this.loadUserByUsername(memberId);

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Wrong password");
        }
        return userDetails;
    }


    @Override
    public  UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return memberRepository.findByMemberId(userId).orElseThrow(() -> new UsernameNotFoundException("Member does not exist"));
    }
}
