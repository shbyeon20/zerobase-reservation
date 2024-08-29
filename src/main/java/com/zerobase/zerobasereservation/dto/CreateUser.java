package com.zerobase.zerobasereservation.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class CreateUser {


    @Getter @Setter
    @AllArgsConstructor
    public static class Request {
        @Size(min=1,max=10)
        private String partnerId;
        @Size(min=1,max=10)
        private String partnerName;
        @Min(1000000000) @Max(9999999999L)
        private Long businessId;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String partnerId;
        private String partnerName;

        public static Response fromDto(UserDto userDto) {
            return Response.builder()
                    .partnerId(userDto.getPartnerId())
                    .partnerName(userDto.getPartnerName())
                    .build();
        }
    }
}
