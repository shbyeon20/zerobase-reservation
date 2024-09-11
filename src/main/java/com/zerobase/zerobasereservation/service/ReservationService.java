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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    /*
    Parameter 정보와 userEntity 및 storeEntity 를 불러오고,
    reservationEntity 에 담아서 REQUESTED Status 로 생성.
     */


    @PreAuthorize("#userId==authentication.principal.id")
    public ReservationDto createReservation(String userId, String storeId,
                                            LocalDateTime reservationTime) {
        log.info("Creating reservation for user {} and store {}", userId, storeId);

        UserEntity userEntity =
                userRepository.findByuserId(userId).orElseThrow
                        (() -> new CustomException(ErrorCode.USERID_NONEXISTENT,
                                "partnerId not existing : " + userId));

        StoreEntity storeEntity =
                storeRepository.findBystoreId(storeId).orElseThrow
                        (() -> new CustomException(ErrorCode.STOREID_NONEXISTENT
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
           생성된 Reservation을 유저가 userId와 storeId를 통해서 조회를 함
     */

    @PreAuthorize("#userId==authentication.principal.id")
    public List<ReservationDto> searchReservationsByUser(String userId,
                                                         String storeId) {
        log.info("Retrieving reservations for user {} and store {}", userId, storeId);
        List<ReservationEntity> reservationEntities =
                reservationRepository.findAllByUserEntity_UserIdAndStoreEntity_StoreIdOrderByReservationTime(userId, storeId);

        return reservationEntities.stream().map(ReservationDto::fromEntity).toList();


    }

    /*
    점주가 예약상태와 관련없이 모든 예약 내역을 조회함
     */
    @PreAuthorize("#partnerId==authentication.principal.id")
    public List<ReservationDto> getReservationsByPartner
            (String partnerId, String storeId) {
        log.info("Retrieving reservations for partner {}", storeId);

        StoreEntity storeEntity = storeRepository.findBystoreId(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STOREID_NONEXISTENT));

        if(!Objects.equals(storeEntity.getPartnerEntity().getPartnerId(), partnerId)){
            throw new CustomException(ErrorCode.PARTNERID_NONEXISTENT);
        }

        List<ReservationEntity> reservationEntities =
                reservationRepository.findAllByStoreEntity_StoreIdOrderByReservationTime(storeId);


        return reservationEntities.stream().map(ReservationDto::fromEntity).toList();

    }

    /*
    점주가 RESERVED 상태인 reservation을 확인하고 REQUESTED를 ACCEPTED로 변경하여
    예약을 확정처리를 하는 절차
     */

    @PreAuthorize("#partnerId==authentication.principal.id")
    public ReservationDto acceptReservation(String partnerId,
                                            String reservationId) {

        log.info("Accepting reservation status for reservation {}",
                reservationId);

        ReservationEntity reservation =
        reservationRepository.findByReservationId(reservationId).orElseThrow
                (() -> new CustomException(ErrorCode.RESERVATION_ID_NONEXISTENT));

        if(!Objects.equals(partnerId, reservation.getStoreEntity().getPartnerEntity().getPartnerId())){
            throw new CustomException(ErrorCode.MEMBERID_STOREOWNER_UNMATCHED);
        }


        if(!reservation.getReservationStatus().equals(ReservationStatus.REQUESTED)) {
            throw new CustomException(ErrorCode.RESERVATION_STATUS_ERROR);
        }

        reservation.setReservationStatus(ReservationStatus.ACCEPTED);

        log.info("Updating reservation status accepted for reservation {}",
                reservationId);

        return ReservationDto.fromEntity(reservationRepository.save(reservation));


    }


    /*
    점주가 RESERVED 상태인 reservation을 확인하고 RESERVED상태를 REJECTED상태로 변경하여
    예약을 확정처리를 하는 절차
     */

    @PreAuthorize("#partnerId==authentication.principal.id")
    public ReservationDto rejectReservation(String partnerId,
                                            String reservationId) {

        log.info("Rejecting reservation status for reservation {}",
                reservationId);

        ReservationEntity reservationEntity =
                reservationRepository.findByReservationId(reservationId).orElseThrow
                        (() -> new CustomException(ErrorCode.RESERVATION_ID_NONEXISTENT));

        if(!Objects.equals(reservationEntity.getStoreEntity().getPartnerEntity().getPartnerId(), partnerId)){
            throw new CustomException(ErrorCode.MEMBERID_STOREOWNER_UNMATCHED);
        }


        if(!reservationEntity.getReservationStatus().equals(ReservationStatus.REQUESTED)) {
            throw new CustomException(ErrorCode.RESERVATION_STATUS_ERROR);
        }

        reservationEntity.setReservationStatus(ReservationStatus.REJECTED);

        log.info("Updating reservation status rejected for reservation {}",
                reservationId);

        return ReservationDto.fromEntity(reservationRepository.save(reservationEntity));


    }


    /*
   고객이 매장의 현장을 방문하여 키오스크를 통해 예약확정을 함. 키오스크이니 인증은 생략.
   단, 1.현재시간이 예약시간의 10분보다 더 많이 남았을 경우
       2. 주인이 예약을 거절하지 않아서 status가 Accepted인 경우에만
       예약확정이 가능함
    */


    public ReservationDto confirmReservation(String reservationId) {

        log.info("Confirming reservation status for reservation {}",
                reservationId);

        ReservationEntity reservationEntity =
                reservationRepository.findByReservationId(reservationId).orElseThrow
                        (() -> new CustomException(ErrorCode.RESERVATION_ID_NONEXISTENT));

        if(!reservationEntity.getReservationStatus().equals(ReservationStatus.ACCEPTED)) {
            throw new CustomException(ErrorCode.RESERVATION_STATUS_ERROR);
        }

        if(LocalDateTime.now().plusMinutes(10).isAfter(reservationEntity.getReservationTime())){
            throw new CustomException(ErrorCode.CONFIRMATION_TOO_LATE);
        }

        reservationEntity.setReservationStatus(ReservationStatus.CONFIRMED);

        log.info("Updating reservation status Confirmed for reservation {}",
                reservationId);

        return ReservationDto.fromEntity(reservationRepository.save(reservationEntity));



    }
}
