package com.zerobase.zerobasereservation.reservation.entity;

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
    private long id;
    @Column(unique = true)
    private String partnerId;
    private String partnerName;
    private long businessId;
    private long phoneNumber;

    private LocalDateTime registeredAt;


}
