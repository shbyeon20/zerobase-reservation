package com.zerobase.zerobasereservation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UpdateReview {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private String reviewId;
        @Size(min = 1, max = 10)
        private String userId;

        @Min(0)
        @Max(5)
        private Integer rating;

        @Size(min = 20, max = 500)
        private String reviewContents;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String reviewId;
        private Integer rating;
        private String reviewContents;

        public static Response fromDto(ReviewDto reviewDto) {
            return Response.builder()
                    .reviewId(reviewDto.getReservationId()) // Assuming reviewId is stored in reservationId
                    .rating(reviewDto.getRating())
                    .reviewContents(reviewDto.getReviewContents())
                    .build();
        }
    }
}