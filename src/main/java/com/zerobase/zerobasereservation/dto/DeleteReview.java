package com.zerobase.zerobasereservation.dto;

import com.zerobase.zerobasereservation.type.ReviewStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class DeleteReview {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private String reviewId;
        @NotNull
        private String memberId;





    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String reviewId;
        private ReviewStatus status;

        public static Response fromDTO(ReviewDto reviewDto) {
            return Response.builder()
                    .reviewId(reviewDto.reviewId)
                    .status(reviewDto.getStatus())
                    .build();
        }
    }
}
