package com.zerobase.zerobasereservation.dto;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class CreatePartner {


    @Getter @Setter
    @AllArgsConstructor
    public static class Request {
        @Size(min=3,max=10)
        private String partnerId;
        @Size(min=3,max=10)
        private String password;
        @Size(min=3,max=10)
        private String partnerName;
        @Size(min=10,max=10)
        private String businessId;
        @Pattern(regexp = "^010\\d{8}$", message = "Phone number must start with 010 and be followed by 8 digits.")
        private String PhoneNumber;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String partnerId;
        private String partnerName;

        public static Response fromDto(PartnerDto partnerDto) {
            return Response.builder()
                    .partnerId(partnerDto.getPartnerId())
                    .partnerName(partnerDto.getPartnerName())
                    .build();
        }
    }
}
