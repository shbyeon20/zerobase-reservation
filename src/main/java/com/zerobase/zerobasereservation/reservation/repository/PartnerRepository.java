package com.zerobase.zerobasereservation.reservation.repository;

import com.zerobase.zerobasereservation.reservation.entity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity, Long > {
    Optional<PartnerEntity> findByPartnerId(String partnerId);
}
