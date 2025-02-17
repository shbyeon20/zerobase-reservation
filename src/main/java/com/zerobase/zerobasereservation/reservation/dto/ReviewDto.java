package com.zerobase.zerobasereservation.reservation.dto;

import com.zerobase.zerobasereservation.reservation.entity.ReviewEntity;
import com.zerobase.zerobasereservation.reservation.type.ReviewStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    String reviewId;
    String userId;
    String reservationId;
    ReviewStatus status;
    String storeId;
    Double rating;
    String reviewContents;

    public static ReviewDto FromEntity(ReviewEntity reviewEntity) {
        return
        ReviewDto.builder()
                .reviewId(reviewEntity.getReviewId())
                .status(reviewEntity.getReviewStatus())
                .userId(reviewEntity.getUserEntity().getUserId())
                .reservationId(reviewEntity.getReservationEntity().getReservationId())
                .storeId(reviewEntity.getStoreEntity().getStoreId())
                .rating(reviewEntity.getRating())
                .reviewContents(reviewEntity.getReviewContents())
                .build();
    }
}
