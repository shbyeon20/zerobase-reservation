package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.entity.MemberDetails;
import com.zerobase.zerobasereservation.exception.CustomException;
import com.zerobase.zerobasereservation.repository.MemberRepository;
import com.zerobase.zerobasereservation.type.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberAuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberAuthService memberAuthService;


    @Test
    void testRegister_Success() {
        String memberId = "testUser";
        String password = "testPassword";
        String encodedPassword = "encodedPassword";

        //given
        given(memberRepository.existsByMemberId(anyString()))
                .willReturn(false);
        given(passwordEncoder.encode(anyString()))
                .willReturn(encodedPassword);

        //when
        memberAuthService.register(memberId, password);

        //then
        ArgumentCaptor<MemberDetails> captor = ArgumentCaptor.forClass(MemberDetails.class);

        verify(memberRepository, times(1))
                .save(captor.capture());

        assertEquals(captor.getValue().getMemberId(),memberId);
        assertEquals(captor.getValue().getPassword(),encodedPassword);
    }

    @Test
    void testRegister_DuplicateMemberId() {
        String memberId = "testUser";
        String password = "testPassword";
        // given
        given(memberRepository.existsByMemberId(memberId))
                .willReturn(true);
        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberAuthService.register(memberId, password);
        });


        assertEquals(ErrorCode.MEMBERID_DUPLICATE, exception.getErrorCode());
        verify(memberRepository, never()).save(any(MemberDetails.class));
    }

    @Test
    void testAuthenticate_Success() {
        String memberId = "testUser";
        String password = "testPassword";
        String encodedPassword = "encodedPassword";

        // given
        MemberDetails memberDetails = MemberDetails.builder()
                .memberId(memberId)
                .password(encodedPassword)
                .build();

        given(memberRepository.findByMemberId(memberId))
                .willReturn(Optional.of(memberDetails));
        given(passwordEncoder.matches(password, encodedPassword))
                .willReturn(true);

        // when
        UserDetails result = memberAuthService.authenticate(memberId, password);

        // then
        assertNotNull(result);
        assertEquals(memberId, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
    }

    @Test
    void testAuthenticate_WrongPassword() {
        String memberId = "testUser";
        String password = "wrongPassword";
        String encodedPassword = "encodedPassword";

        // given
        MemberDetails memberDetails = MemberDetails.builder()
                .memberId(memberId)
                .password(encodedPassword)
                .build();

        given(memberRepository.findByMemberId(memberId))
                .willReturn(Optional.of(memberDetails));
        given(passwordEncoder.matches(password, encodedPassword))
                .willReturn(false);

        // when
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            memberAuthService.authenticate(memberId, password);
        });

        // then
        assertEquals("Wrong password", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_Success() {
        String memberId = "testUser";
        String encodedPassword = "encodedPassword";

        // given
        MemberDetails memberDetails = MemberDetails.builder()
                .memberId(memberId)
                .password(encodedPassword)
                .build();

        given(memberRepository.findByMemberId(memberId))
                .willReturn(Optional.of(memberDetails));

        // when
        UserDetails result = memberAuthService.loadUserByUsername(memberId);

        // then
        assertNotNull(result);
        assertEquals(memberId, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        String memberId = "nonExistentUser";

        // given
        given(memberRepository.findByMemberId(memberId))
                .willReturn(Optional.empty());

        // when
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            memberAuthService.loadUserByUsername(memberId);
        });

        // then
        assertEquals("Member does not exist", exception.getMessage());
    }


}


