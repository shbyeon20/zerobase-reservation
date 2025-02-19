package com.zerobase.zerobasereservation.reservation.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.zerobasereservation.reservation.entity.StoreEntity;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {
    private String partnerId;
    private String storeId;
    private String address;
    private String storeComment;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static StoreDto fromEntity(StoreEntity storeEntity) {
        return StoreDto.builder()
                .partnerId(storeEntity.getPartnerEntity().getPartnerId())
                .storeId(storeEntity.getStoreId())
                .address(storeEntity.getAddress())
                .storeComment(storeEntity.getStoreComment())
                .build();
    }


    public static StoreDto fromJson(String storeData) {
        try {
            return objectMapper.readValue(storeData, StoreDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    public static String toJson(StoreDto storeDto) {
        try {
            return objectMapper.writeValueAsString(storeDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting to JSON", e);
        }
    }
}
