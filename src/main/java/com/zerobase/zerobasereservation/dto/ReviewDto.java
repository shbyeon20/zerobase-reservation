package com.zerobase.zerobasereservation.dto;

import com.zerobase.zerobasereservation.entity.ReviewEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    String userId;
    String reservationId;
    String storeId;
    Integer rating;
    String reviewContents;

    public static ReviewDto FromEntity(ReviewEntity reviewEntity) {
        return
        ReviewDto.builder()
                .userId(reviewEntity.getUserEntity().getUserId())
                .reservationId(reviewEntity.getReservationEntity().getReservationId())
                .storeId(reviewEntity.getStoreEntity().getStoreId())
                .rating(reviewEntity.getRating())
                .reviewContents(reviewEntity.getReviewContents())
                .build();
    }
}
