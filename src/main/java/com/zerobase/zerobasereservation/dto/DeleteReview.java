package com.zerobase.zerobasereservation.dto;

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

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String reviewId;
        private String status;

        public static Response success(String reviewId) {
            return Response.builder()
                    .reviewId(reviewId)
                    .status("Deleted successfully")
                    .build();
        }
    }
}
