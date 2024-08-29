package com.zerobase.zerobasereservation.dto;

import com.zerobase.zerobasereservation.entity.StoreEntity;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
    private String partnerId;
    private String storeId;
    private String address;
    private String storeComment;

    public static StoreDto fromEntity(StoreEntity storeEntity) {
        return StoreDto.builder()
                .partnerId(storeEntity.getPartnerEntity().getPartnerId())
                .storeId(storeEntity.getStoreId())
                .address(storeEntity.getAddress())
                .storeComment(storeEntity.getStoreComment())
                .build();
    }



}
