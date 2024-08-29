package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.StoreDto;
import com.zerobase.zerobasereservation.entity.PartnerEntity;
import com.zerobase.zerobasereservation.entity.StoreEntity;
import com.zerobase.zerobasereservation.exception.PartnerException;
import com.zerobase.zerobasereservation.exception.StoreException;
import com.zerobase.zerobasereservation.repository.PartnerRepository;
import com.zerobase.zerobasereservation.repository.StoreRepository;
import com.zerobase.zerobasereservation.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StoreService {
    private final StoreRepository storeRepository;
    private final PartnerRepository partnerRepository;

    public StoreDto createStore(String partnerId, String storeId,
                                String address, String storeComment) {
        log.info("store creation service start");
        log.info("Find by partnerId for partnerEntity :" +partnerId  );
        PartnerEntity partnerEntity =
                partnerRepository.findBypartnerId(partnerId)
                        .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_ID_NONEXISTENT, "존재하지 않는 partnerID입니다 : " + partnerId));

        return StoreDto.fromEntity(storeRepository.save(
                        StoreEntity.builder()
                                .partnerEntity(partnerEntity)
                                .storeId(storeId)
                                .address(address)
                                .storeComment(storeComment)
                                .registeredAt(LocalDateTime.now())
                                .build()
                )
        );

    }

    public List<StoreDto> findByPartnerId(String partnerId) {
        log.info("find Store using partnerID :"+partnerId);
        PartnerEntity partnerEntity = partnerRepository.findBypartnerId(partnerId)
                        .orElseThrow(() -> new PartnerException(ErrorCode.PARTNER_ID_NONEXISTENT, "존재하지 않는 partnerID입니다 : " + partnerId));
        List<StoreEntity> storeEntities =
                storeRepository.findAllByPartnerEntity(partnerEntity);

        return storeEntities.stream().map(StoreDto::fromEntity).toList();
    }



    public StoreDto findByStoreId(String storeId) {
        log.info("find Store using storeID :"+storeId);
        return StoreDto.fromEntity(storeRepository.findBystoreId(storeId)
                .orElseThrow(()->new StoreException(
                        ErrorCode.STORE_ID_NONEXISTENT,
                        "존재하지 않는 storeId 입니다 : "+storeId)));



    }
}
