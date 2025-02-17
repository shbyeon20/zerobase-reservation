package com.zerobase.zerobasereservation.reservation.dto;


import com.zerobase.zerobasereservation.reservation.type.ReservationStatus;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;


public class UpdateStatusReservation {

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Size(min=1,max=10)
        private String memberId;
        private ReservationStatus status;



    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private String reservationId;
        private String userId;
        private String storeId;
        private LocalDateTime reservationTime;
        private ReservationStatus status;


        public static Response fromDto(ReservationDto reservationDto) {
            return Response.builder()
                    .reservationId(reservationDto.getReservationId())
                    .userId(reservationDto.getUserID())
                    .storeId(reservationDto.getStoreId())
                    .reservationTime(reservationDto.getReservationTime())
                    .status(reservationDto.getStatus())
                    .build();
        }
    }
}
