package com.zerobase.zerobasereservation.repository;

import com.zerobase.zerobasereservation.entity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity, Long > {
    Optional<PartnerEntity> findBypartnerId(String partnerId);
}
