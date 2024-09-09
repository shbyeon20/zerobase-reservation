package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.ReservationDto;
import com.zerobase.zerobasereservation.entity.ReservationEntity;
import com.zerobase.zerobasereservation.entity.StoreEntity;
import com.zerobase.zerobasereservation.entity.UserEntity;
import com.zerobase.zerobasereservation.exception.CustomException;
import com.zerobase.zerobasereservation.repository.ReservationRepository;
import com.zerobase.zerobasereservation.repository.StoreRepository;
import com.zerobase.zerobasereservation.repository.UserRepository;
import com.zerobase.zerobasereservation.type.ErrorCode;
import com.zerobase.zerobasereservation.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    /*
    연관 entity인 Userentity와 storeentity를 불러오고, reservationentity에 담아서 생성
     */

    public ReservationDto createReservation(String userId, String storeId,
                                            LocalDateTime reservationTime) {
        log.info("Creating reservation for user {} and store {}", userId, storeId);

        UserEntity userEntity =
                userRepository.findByuserId(userId).orElseThrow
                        (() -> new CustomException(ErrorCode.USER_ID_NONEXISTENT,
                                "partnerId not existing : " + userId));

        StoreEntity storeEntity =
                storeRepository.findBystoreId(storeId).orElseThrow
                        (() -> new CustomException(ErrorCode.STORE_ID_NONEXISTENT
                                , "partnerId not existing : " + storeId));


        ReservationEntity reservationEntity = reservationRepository.save(ReservationEntity
                .builder()
                .reservationId(RandomStringUtils.randomAlphanumeric(8))
                .userEntity(userEntity)
                .storeEntity(storeEntity)
                .reservationStatus(ReservationStatus.REQUESTED)
                .reservationTime(reservationTime)
                .createdAt(LocalDateTime.now())
                .build());

        return ReservationDto.fromEntity(reservationEntity);
    }

    /*
    고객이 매장의 현장을 방문하여 키오스크를 통해 예약확정을 함
    단, 1.현재시간이 예약시간의 10분보다 더 많이 남았을 경우
        2. 주인이 예약을 거절하지 않아서 status가 Accepted인 경우에만
        예약확정이 가능함
     */





    /*
           생성된 Reservation을 유저가 userId와 storeId를 통해서 조회를 함
     */

    public List<ReservationDto> getReservationsByUser(String userId,
                                                      String storeId) {
        log.info("Retrieving reservations for user {} and store {}", userId, storeId);
        List<ReservationEntity> reservationEntities =
                reservationRepository.findAllByUserEntity_UserIdAndStoreEntity_StoreIdOrderByReservationTime(userId, storeId);

        return reservationEntities.stream().map(ReservationDto::fromEntity).toList();


    }

    /*
    점주가 예약상태와 관련없이 모든 예약 내역을 조회함
     */

    public List<ReservationDto> getReservationsByPartner(String storeId) {
        log.info("Retrieving reservations for partner {}", storeId);

        List<ReservationEntity> reservationEntities =
                reservationRepository.findAllByStoreEntity_StoreIdOrderByReservationTime(storeId);

        return reservationEntities.stream().map(ReservationDto::fromEntity).toList();

    }

    /*
    점주가 RESERVED 상태인 reservation을 확인하고 REQUESTED를 ACCEPTED로 변경하여
    예약을 확정처리를 하는 절차
     */

    public ReservationDto acceptReservation(String reservationId) {

        log.info("Confirming reservation status for reservation {}",
                reservationId);

        ReservationEntity reservationToConfirm =
        reservationRepository.findByReservationId(reservationId).orElseThrow
                (() -> new CustomException(ErrorCode.RESERVATION_ID_NONEXISTENT));


        if(!reservationToConfirm.getReservationStatus().equals(ReservationStatus.REQUESTED)) {
            throw new CustomException(ErrorCode.RESERVATION_STATUS_ERROR);
        }

        reservationToConfirm.setReservationStatus(ReservationStatus.ACCEPTED);

        log.info("Updating reservation status accepted for reservation {}",
                reservationId);

        return ReservationDto.fromEntity(reservationRepository.save(reservationToConfirm));


    }


    /*
    점주가 RESERVED 상태인 reservation을 확인하고 RESERVED상태를 REJECTED상태로 변경하여
    예약을 확정처리를 하는 절차
     */

    public ReservationDto rejectReservation(String reservationId) {

        log.info("Rejecting reservation status for reservation {}",
                reservationId);

        ReservationEntity reservationToConfirm =
                reservationRepository.findByReservationId(reservationId).orElseThrow
                        (() -> new CustomException(ErrorCode.RESERVATION_ID_NONEXISTENT));

        if(!reservationToConfirm.getReservationStatus().equals(ReservationStatus.REQUESTED)) {
            throw new CustomException(ErrorCode.RESERVATION_STATUS_ERROR);
        }

        reservationToConfirm.setReservationStatus(ReservationStatus.REJECTED);

        log.info("Updating reservation status rejected for reservation {}",
                reservationId);

        return ReservationDto.fromEntity(reservationRepository.save(reservationToConfirm));


    }


    /*
      user가 키오스크에 방문하여
     */
    public ReservationDto confirmReservation(String reservationId) {

        log.info("Rejecting reservation status for reservation {}",
                reservationId);

        ReservationEntity reservationToConfirm =
                reservationRepository.findByReservationId(reservationId).orElseThrow
                        (() -> new CustomException(ErrorCode.RESERVATION_ID_NONEXISTENT));

        if(!reservationToConfirm.getReservationStatus().equals(ReservationStatus.REQUESTED)) {
            throw new CustomException(ErrorCode.RESERVATION_STATUS_ERROR);
        }

        reservationToConfirm.setReservationStatus(ReservationStatus.REJECTED);

        log.info("Updating reservation status rejected for reservation {}",
                reservationId);

        return ReservationDto.fromEntity(reservationRepository.save(reservationToConfirm));



    }
}
