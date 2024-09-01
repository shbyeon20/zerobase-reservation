package com.zerobase.zerobasereservation.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class CreateReview {


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {

        @Size(min = 1, max = 10)
        private String userId;
        @Min(0)
        @Max(5)
        Integer rating;
        private String reservationId;
        @Size(min = 20, max = 500)
        String reviewContents;


    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Response {
        String userId;
        String reservationId;
        String storeId;
        Integer rating;
        String reviewContents;


        public static Response fromDto(ReviewDto reviewDto) {
            return Response.builder()
                    .userId(reviewDto.getUserId())
                    .reservationId(reviewDto.getReservationId())
                    .storeId(reviewDto.getStoreId())
                    .rating(reviewDto.getRating())
                    .reviewContents(reviewDto.getReviewContents())
                    .build();
        }
    }
}
