package com.zerobase.zerobasereservation.reservation.entity;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "partnerId", referencedColumnName = "partnerId",
            nullable = false)
    private PartnerEntity partnerEntity;
    @Column(unique = true)
    private String storeId;
    private String address;
    private String storeComment;
    private double rating;
    private LocalDateTime registeredAt;

}
