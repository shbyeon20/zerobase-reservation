package com.zerobase.zerobasereservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String partnerId;
    private String partnerName;
    private String businessId;
    private String phoneNumber;

    private LocalDateTime registeredAt;


}
