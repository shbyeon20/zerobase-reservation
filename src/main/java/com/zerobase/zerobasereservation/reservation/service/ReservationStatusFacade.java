package com.zerobase.zerobasereservation.reservation.service;

import com.zerobase.zerobasereservation.reservation.dto.ReservationDto;
import com.zerobase.zerobasereservation.reservation.exception.CustomException;
import com.zerobase.zerobasereservation.reservation.type.ErrorCode;
import com.zerobase.zerobasereservation.reservation.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationStatusFacade {

    private final ReservationService reservationService;
    private final NotificationProducer notificationProducer;


    public ReservationDto readStatusAndAssignService(String memberId, String reservationId, ReservationStatus status) {
        if(status == ReservationStatus.CONFIRMED) {
            return reservationService.confirmReservation(memberId,reservationId);
        }
        else if(status == ReservationStatus.ACCEPTED) {
            return reservationService.acceptReservation(memberId,reservationId);
        } else if (status == ReservationStatus.REJECTED) {
            return this.rejectReservation(memberId,reservationId);
        }
        throw new CustomException(ErrorCode.RESERVATION_STATUS_ERROR);
    }

    @Transactional
    public ReservationDto rejectReservation(String memberId, String reservationId) {

        ReservationDto reservationDto = reservationService.rejectReservation(memberId, reservationId);
        notificationProducer.sendReservationRejectNotification(memberId,reservationId);

        return reservationDto;

    }

}
