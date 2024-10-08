package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.StoreDto;
import com.zerobase.zerobasereservation.entity.PartnerEntity;
import com.zerobase.zerobasereservation.entity.StoreEntity;
import com.zerobase.zerobasereservation.exception.CustomException;
import com.zerobase.zerobasereservation.repository.PartnerRepository;
import com.zerobase.zerobasereservation.repository.ReviewRepository;
import com.zerobase.zerobasereservation.repository.StoreRepository;
import com.zerobase.zerobasereservation.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StoreService {
    private final StoreRepository storeRepository;
    private final PartnerRepository partnerRepository;
    private final ReviewRepository reviewRepository;

    @PreAuthorize("#partnerId == authentication.principal.memberId")
    public StoreDto createStore(String partnerId, String storeId,
                                String address, String storeComment) {
        log.info("store creation service start");
        log.info("Find by partnerId for partnerEntity :" +partnerId  );

        PartnerEntity partnerEntity =
                partnerRepository.findBypartnerId(partnerId)
                        .orElseThrow(() -> new CustomException(ErrorCode.PARTNERID_NONEXISTENT));




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

    @PreAuthorize("#partnerId == authentication.principal.memberId")
    public List<StoreDto> findByPartnerId(String partnerId) {
        log.info("find Store using partnerID :"+partnerId);
        PartnerEntity partnerEntity = partnerRepository.findBypartnerId(partnerId)
                        .orElseThrow(() -> new CustomException(ErrorCode.PARTNERID_NONEXISTENT));
        List<StoreEntity> storeEntities =
                storeRepository.findAllByPartnerEntity(partnerEntity);

        return storeEntities.stream().map(StoreDto::fromEntity).toList();
    }


    /*
    storeId로 등록된 매장레코드를 조회하여 매장에 관한 정보를 Web으로 반환함
     */

    @PreAuthorize("#partnerId == authentication.principal.memberId")
    public StoreDto findByStoreId(String partnerId, String storeId) {
        log.info("find Store using storeID :"+storeId);

        StoreEntity storeEntity = storeRepository.findBystoreId(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STOREID_NONEXISTENT));

        if(!Objects.equals(storeEntity.getPartnerEntity().getPartnerId(), partnerId)) {
            throw new CustomException(ErrorCode.MEMBERID_STOREOWNER_UNMATCHED);
        }

        return StoreDto.fromEntity(storeEntity);
    }



    /*
    storeId로 등록된 review record를 조회하여 storeentity의 rating을 최신화함
    ! I/O burden을 줄이기 위해서 DB 내에서 rating에 대해서만 전송하도록 전처리 진행
     */

    public void updateRating(StoreEntity storeEntity) {
        log.info("update rating using storeID :"+storeEntity.getStoreId());

        Double rating = reviewRepository.reupdateAverageRating(storeEntity);
        storeEntity.setRating(rating);
        storeRepository.save(storeEntity);
        log.info("update rating using storeID :"+storeEntity.getStoreId());
    }


}
