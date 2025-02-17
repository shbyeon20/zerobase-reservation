package com.zerobase.zerobasereservation.reservation.service;

import com.zerobase.zerobasereservation.reservation.dto.UserDto;
import com.zerobase.zerobasereservation.reservation.entity.UserEntity;
import com.zerobase.zerobasereservation.reservation.repository.UserRepository;
import com.zerobase.zerobasereservation.reservation.type.ROLE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDataManager {

    private final UserRepository userRepository;
    private final UserDetailsImpl userDetailsImpl;

    /*
    memberEntity 와 userEntity 를 생성하고 저장함.
     */

    public UserDto createUser(String userId, String password,
                              String userName, long phoneNumber) {

        log.info("Create member with id {}", userId);
        userDetailsImpl.register(userId, password, ROLE.ROLE_USER);

        log.info("createUser service layer started : "+userId);
        UserEntity userEntity = userRepository.save(
                UserEntity.builder()
                        .userId(userId)
                        .userName(userName)
                        .phoneNumber(phoneNumber)
                        .registeredAt(LocalDateTime.now())
                        .build()
        );
        log.info("createUser service layer started : "+userEntity.getUserId());


        return UserDto.from(userEntity);
    }
}
