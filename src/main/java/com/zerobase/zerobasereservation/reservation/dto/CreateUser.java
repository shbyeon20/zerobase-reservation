package com.zerobase.zerobasereservation.reservation.dto;


import jakarta.validation.constraints.NotNull;
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
        @NotNull
        @Size(min=3,max=10)
        private String userId;
        @NotNull
        @Size(min=3,max=10)
        private String password;
        @NotNull
        @Size(min=3,max=10)
        private String userName;
        @NotNull
        private long phoneNumber;

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
