package com.zerobase.zerobasereservation.entity;


import com.zerobase.zerobasereservation.type.ReviewStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String reviewID;
    @OneToOne
    ReservationEntity reservationEntity;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId",
            nullable = false)
    UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "storeId", referencedColumnName = "storeId",
            nullable = false)
    StoreEntity storeEntity;

    Integer rating;

    String reviewContents;
    @Enumerated(EnumType.STRING)
    ReviewStatus reviewStatus;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}
