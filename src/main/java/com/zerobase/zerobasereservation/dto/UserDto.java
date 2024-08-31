package com.zerobase.zerobasereservation.dto;

import com.zerobase.zerobasereservation.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {
    private String userId;
    private String userName;

    public static UserDto from(UserEntity userEntity) {
        return UserDto.builder()
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .build();
    }
}
