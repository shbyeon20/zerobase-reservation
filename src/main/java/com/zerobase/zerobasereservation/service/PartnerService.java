package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.PartnerDto;
import com.zerobase.zerobasereservation.entity.PartnerEntity;
import com.zerobase.zerobasereservation.repository.PartnerRepository;
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
public class PartnerService {
    private final PartnerRepository partnerRepository;

    @Transactional
    public PartnerDto createPartner(String partnerId,  String partnerName,
                              Long businessId) {
        log.info("Create partner with id {} and name {}", partnerId, partnerName);

        // todo : 중복된 데이터에 대한 exception handling 처리할 것
        PartnerEntity partnerEntity = partnerRepository.save(
                PartnerEntity.builder().
                partnerId(partnerId)
                .partnerName(partnerName)
                .businessId(businessId)
                .registeredAt(LocalDateTime.now())
                .build()
        );

        return PartnerDto.fromEntity(partnerEntity);
    }
}
