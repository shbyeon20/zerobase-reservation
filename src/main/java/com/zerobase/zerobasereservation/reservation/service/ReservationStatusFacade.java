package com.zerobase.zerobasereservation.reservation.service;

import com.zerobase.zerobasereservation.reservation.dto.ReservationDto;
import com.zerobase.zerobasereservation.reservation.exception.CustomException;
import com.zerobase.zerobasereservation.reservation.type.ErrorCode;
import com.zerobase.zerobasereservation.reservation.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationStatusFacade {

    private final ReservationService reservationService;


    public ReservationDto readStatusAndAssignService(String memberId, String reservationId, ReservationStatus status) {
        if(status == ReservationStatus.CONFIRMED) {
            return reservationService.confirmReservation(memberId,reservationId);
        }
        else if(status == ReservationStatus.ACCEPTED) {
            return reservationService.acceptReservation(memberId,reservationId);
        } else if (status == ReservationStatus.REJECTED) {
            return reservationService.rejectReservation(memberId,reservationId);
        }
        throw new CustomException(ErrorCode.RESERVATION_STATUS_ERROR);
    }

}
