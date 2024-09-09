package com.zerobase.zerobasereservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StoreEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partnerId", referencedColumnName = "partnerId",
            nullable = false)
    private PartnerEntity partnerEntity;
    @Column(unique = true)
    private String storeId;
    private String address;
    private String storeComment;
    private Double rating;
    private LocalDateTime registeredAt;

}
