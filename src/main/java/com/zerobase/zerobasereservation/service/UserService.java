package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.UserDto;
import com.zerobase.zerobasereservation.entity.UserEntity;
import com.zerobase.zerobasereservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(String userId,
                              String userName, String phoneNumber) {
        log.info("createUser service layer started : "+userId);
        UserEntity userEntity = userRepository.save(
                UserEntity.builder()
                        .userId(userId)
                        .userName(userName)
                        .PhoneNumber(phoneNumber)
                        .registrationDate(LocalDateTime.now())
                        .build()
        );
        log.info("createUser service layer started : "+userEntity.getUserId());


        return UserDto.from(userEntity);
    }
}
