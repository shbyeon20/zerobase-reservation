package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.UserDto;
import com.zerobase.zerobasereservation.entity.UserEntity;
import com.zerobase.zerobasereservation.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemberAuthService memberAuthService;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldCreateUserSuccessfully() {
        // Given
        String userId = "john_doe";
        String password = "securePassword123";
        String userName = "John Doe";
        String phoneNumber = "555-1234";

        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .userName(userName)
                .phoneNumber(phoneNumber)
                .registrationDate(LocalDateTime.now())
                .build();

        // Mock the userRepository.save method to return the userEntity
        given(userRepository.save(any(UserEntity.class))).willReturn(userEntity);

        // When
        UserDto result = userService.createUser(userId, password, userName, phoneNumber);

        // Then
        // Verify that memberAuthService.register was called with correct parameters
        then(memberAuthService).should().register(userId, password);

        // Capture the UserEntity passed to userRepository.save
        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        then(userRepository).should().save(userEntityCaptor.capture());

        UserEntity capturedUserEntity = userEntityCaptor.getValue();

        // Assert that the captured UserEntity has the expected values
        assertEquals(userId, capturedUserEntity.getUserId());
        assertEquals(userName, capturedUserEntity.getUserName());
        assertEquals(phoneNumber, capturedUserEntity.getPhoneNumber());
        assertNotNull(capturedUserEntity.getRegistrationDate());

        // Assert that the result UserDto has the expected values
        assertEquals(userId, result.getUserId());
        assertEquals(userName, result.getUserName());
    }
}
