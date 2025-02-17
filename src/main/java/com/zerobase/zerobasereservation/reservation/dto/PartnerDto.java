package com.zerobase.zerobasereservation.reservation.dto;


import com.zerobase.zerobasereservation.reservation.entity.PartnerEntity;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartnerDto {

    private String partnerId;
    private String partnerName;


    public static PartnerDto fromEntity(PartnerEntity partnerEntity) {
        return PartnerDto.builder()
                .partnerId(partnerEntity.getPartnerId())
                .partnerName(partnerEntity.getPartnerName())
                .build();
    }
}

