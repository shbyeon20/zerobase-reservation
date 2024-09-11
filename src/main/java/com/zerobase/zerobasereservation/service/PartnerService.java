package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.PartnerDto;
import com.zerobase.zerobasereservation.entity.PartnerEntity;
import com.zerobase.zerobasereservation.repository.PartnerRepository;
import com.zerobase.zerobasereservation.security.MemberAuthService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final MemberAuthService memberAuthService;


    /*
    memberEntity 와 partnerEntity 를 생성하고 저장함.
     */

    public PartnerDto createPartner(String partnerId, String password, String partnerName,
                                    String businessId, String phoneNumber) {

        log.info("Create member with id {}", partnerId);
        memberAuthService.register(partnerId, password);

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
