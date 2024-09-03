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
                .reservationStatus(ReservationStatus.RESERVED)
                .reservationTime(reservationTime)
                .createdAt(LocalDateTime.now())
                .build());

        return ReservationDto.fromEntity(reservationEntity);
    }

    public List<ReservationDto> getReservationsByUser(String userId,
                                                      String storeId) {
        log.info("Retrieving reservations for user {} and store {}", userId, storeId);
        List<ReservationEntity> reservationEntities =
                reservationRepository.findAllByUserEntity_UserIdAndStoreEntity_StoreIdOrderByReservationTime(userId, storeId);

        return reservationEntities.stream().map(ReservationDto::fromEntity).toList();


    }

    public List<ReservationDto> getReservationsByPartner(String storeId) {
        log.info("Retrieving reservations for partner {}", storeId);

        List<ReservationEntity> reservationEntities =
                reservationRepository.findAllByStoreEntity_StoreIdOrderByReservationTime(storeId);

        return reservationEntities.stream().map(ReservationDto::fromEntity).toList();

    }

    public ReservationDto confirmReservation(String reservationId) {

        log.info("Confirming reservation status for reservation {}",
                reservationId);

        ReservationEntity reservationToConfirm =
        reservationRepository.findByReservationId(reservationId).orElseThrow
                (() -> new CustomException(ErrorCode.RESERVATION_ID_NONEXISTENT));


        if(!reservationToConfirm.getReservationStatus().equals(ReservationStatus.RESERVED)) {
            throw new CustomException(ErrorCode.RESERVATION_STATUS_ERROR);
        }

        reservationToConfirm.setReservationStatus(ReservationStatus.CONFIRMED);

        log.info("Updating reservation status confirmed for reservation {}", reservationId);

        return ReservationDto.fromEntity(reservationRepository.save(reservationToConfirm));


    }

    public ReservationDto rejectReservation(String reservationId) {

        log.info("Rejecting reservation status for reservation {}",
                reservationId);

        ReservationEntity reservationToConfirm =
                reservationRepository.findByReservationId(reservationId).orElseThrow
                        (() -> new CustomException(ErrorCode.RESERVATION_ID_NONEXISTENT));

        if(!reservationToConfirm.getReservationStatus().equals(ReservationStatus.RESERVED)) {
            throw new CustomException(ErrorCode.RESERVATION_STATUS_ERROR);
        }

        reservationToConfirm.setReservationStatus(ReservationStatus.REJECTED);

        log.info("Updating reservation status confirmed for reservation {}", reservationId);

        return ReservationDto.fromEntity(reservationRepository.save(reservationToConfirm));


    }
}
