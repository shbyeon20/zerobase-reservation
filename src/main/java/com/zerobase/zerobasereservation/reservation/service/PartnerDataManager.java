package com.zerobase.zerobasereservation.reservation.service;

import com.zerobase.zerobasereservation.reservation.dto.PartnerDto;
import com.zerobase.zerobasereservation.reservation.entity.PartnerEntity;
import com.zerobase.zerobasereservation.reservation.repository.PartnerRepository;
import com.zerobase.zerobasereservation.reservation.type.ROLE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PartnerDataManager {
    private final PartnerRepository partnerRepository;
    private final UserDetailsImpl userDetailsImpl;


    /*
    memberEntity 와 partnerEntity 를 생성하고 저장함.
     */

    public PartnerDto createPartner(String partnerId, String password, String partnerName,
                                    long businessId, long phoneNumber) {

        log.info("Create member with id {}", partnerId);
        userDetailsImpl.register(partnerId, password, ROLE.ROLE_PARTNER);

        log.info("Create partner with id {} and name {}", partnerId, partnerName);

        PartnerEntity partnerEntity = partnerRepository.save(
                PartnerEntity.builder().
                        partnerId(partnerId)
                        .partnerName(partnerName)
                        .businessId(businessId)
                        .phoneNumber(phoneNumber)
                        .registeredAt(LocalDateTime.now())
                        .build()
        );

        return PartnerDto.fromEntity(partnerEntity);
    }
}
