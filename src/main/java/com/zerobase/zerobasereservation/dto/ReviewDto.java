package com.zerobase.zerobasereservation.dto;

import com.zerobase.zerobasereservation.entity.ReviewEntity;
import com.zerobase.zerobasereservation.type.ReviewStatus;
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
    Integer rating;
    String reviewContents;

    public static ReviewDto FromEntity(ReviewEntity reviewEntity) {
        return
        ReviewDto.builder()
                .reviewId(reviewEntity.getReviewID())
                .status(reviewEntity.getReviewStatus())
                .userId(reviewEntity.getUserEntity().getUserId())
                .reservationId(reviewEntity.getReservationEntity().getReservationId())
                .storeId(reviewEntity.getStoreEntity().getStoreId())
                .rating(reviewEntity.getRating())
                .reviewContents(reviewEntity.getReviewContents())
                .build();
    }
}
