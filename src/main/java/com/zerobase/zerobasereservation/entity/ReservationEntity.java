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
    String reservationId;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId",
            nullable = false)
    UserEntity userEntity;
    @ManyToOne
    @JoinColumn(name = "storeID", referencedColumnName = "storeID",
            nullable = false)
    StoreEntity storeEntity;
    ReservationStatus reservationStatus;
    LocalDateTime reservationTime;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
