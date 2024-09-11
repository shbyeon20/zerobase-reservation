package com.zerobase.zerobasereservation.dto;


import com.zerobase.zerobasereservation.type.ReservationStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class GetReservationsByPartner {


    @Getter @Setter
    @AllArgsConstructor
    public static class Request {

        @Size(min=1,max=10)
        private String storeId;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String reservationId;
        private String storeId;
        private LocalDateTime reservationTime;
        private ReservationStatus status;

        public static Response fromDto(ReservationDto reservationDto) {
            return Response.builder()
                    .reservationId(reservationDto.getReservationId())
                    .storeId(reservationDto.getStoreId())
                    .reservationTime(reservationDto.getReservationTime())
                    .status(reservationDto.getStatus())
                    .build();
        }
    }
}
