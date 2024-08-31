package com.zerobase.zerobasereservation.dto;


import com.zerobase.zerobasereservation.entity.ReservationEntity;
import com.zerobase.zerobasereservation.type.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationDto {

    private String reservationId;
    private String userID;
    private String storeId;
    private LocalDateTime reservationTime;
    private ReservationStatus status;




    public static ReservationDto fromEntity(ReservationEntity reservationEntity) {
        return ReservationDto.builder()
                .reservationId(reservationEntity.getReservationId())
                .userID(reservationEntity.getUserEntity().getUserId())
                .storeId(reservationEntity.getStoreEntity().getStoreId())
                .reservationTime(reservationEntity.getReservationTime())
                .status(reservationEntity.getReservationStatus())
                .build();
    }
}

