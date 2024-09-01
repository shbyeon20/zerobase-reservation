package com.zerobase.zerobasereservation.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class CreateReservation {


    @Getter @Setter
    @AllArgsConstructor
    public static class Request {


        @Size(min=1,max=10)
        private String userId;
        @Size(min=1,max=10)
        private String storeId;
        @NotNull
        private LocalDateTime reservationTime;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String reservationId;
        private String userId;
        private String storeId;
        private LocalDateTime reservationTime;


        public static Response fromDto(ReservationDto reservationDto) {
            return Response.builder()
                    .reservationId(reservationDto.getReservationId())
                    .userId(reservationDto.getUserID())
                    .storeId(reservationDto.getStoreId())
                    .reservationTime(reservationDto.getReservationTime())
                    .build();
        }
    }
}
