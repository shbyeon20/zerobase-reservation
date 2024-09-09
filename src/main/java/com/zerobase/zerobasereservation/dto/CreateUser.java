package com.zerobase.zerobasereservation.dto;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class CreateUser {


    @Getter @Setter
    @AllArgsConstructor
    public static class Request {
        @Size(min=3,max=10)
        private String userId;
        @Size(min=3,max=10)
        private String password;
        @Size(min=3,max=10)
        private String userName;

        @Pattern(regexp = "^010\\d{8}$", message = "Phone number must start with 010 and be followed by 8 digits.")
        private String phoneNumber;

        private Long registeredAt;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String userId;
        private String userName;

        public static Response fromDto(UserDto userDto) {
            return Response.builder()
                    .userId(userDto.getUserId())
                    .userName(userDto.getUserName())
                    .build();
        }
    }
}
