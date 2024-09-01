package com.zerobase.zerobasereservation.repository;

import com.zerobase.zerobasereservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long >
{
    List<ReservationEntity> findAllByUserEntity_UserIdAndStoreEntity_StoreIdOrderByReservationTime(String userId,
                                                                                                   String storeId);

    List<ReservationEntity> findAllByStoreEntity_StoreIdOrderByReservationTime(String storeId);

    Optional<ReservationEntity> findByReservationId(String reservationId);


}
