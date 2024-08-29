package com.zerobase.zerobasereservation.repository;

import com.zerobase.zerobasereservation.entity.PartnerEntity;
import com.zerobase.zerobasereservation.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long > {
    List<StoreEntity> findAllByPartnerEntity(PartnerEntity partnerEntity);

    Optional<StoreEntity> findBystoreId(String storeId);
}
