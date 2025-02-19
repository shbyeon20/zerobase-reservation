package com.zerobase.zerobasereservation.alarmstore;


import com.zerobase.zerobasereservation.reservation.entity.ReservationEntity;
import com.zerobase.zerobasereservation.reservation.entity.StoreEntity;
import com.zerobase.zerobasereservation.reservation.entity.UserEntity;
import com.zerobase.zerobasereservation.reservation.type.ReviewStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(unique = true)
    String memberId;

    String message;


    LocalDateTime createdAt;


}
