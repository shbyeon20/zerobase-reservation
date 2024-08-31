package com.zerobase.zerobasereservation.entity;


import com.zerobase.zerobasereservation.type.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(unique = true)
    String reservationId;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId",
            nullable = false)
    UserEntity userEntity;
    @ManyToOne
    @JoinColumn(name = "storeId", referencedColumnName = "storeId",
            nullable = false)
    StoreEntity storeEntity;
    @Enumerated(EnumType.STRING)
    ReservationStatus reservationStatus;
    LocalDateTime reservationTime;
    LocalDateTime createdAt;


}
