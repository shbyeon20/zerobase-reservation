package com.zerobase.zerobasereservation.repository;

import com.zerobase.zerobasereservation.entity.ReservationEntity;
import com.zerobase.zerobasereservation.entity.UserEntity;
import com.zerobase.zerobasereservation.type.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long >
{
    List<ReservationEntity> findAllByUserIdAndStoreIdOrderByReservationTime(String userId,
                                                       String storeId);

    List<ReservationEntity> findAllByStoreIdOrderByReservationTime(String storeId);

    Optional<ReservationEntity> findByReservationId(String reservationId);


}
