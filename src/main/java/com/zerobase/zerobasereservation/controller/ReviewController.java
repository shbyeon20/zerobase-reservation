package com.zerobase.zerobasereservation.controller;

import com.zerobase.zerobasereservation.dto.CreateReview;
import com.zerobase.zerobasereservation.dto.DeleteReview;
import com.zerobase.zerobasereservation.dto.ReviewDto;
import com.zerobase.zerobasereservation.dto.UpdateReview;
import com.zerobase.zerobasereservation.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;


    //todo : validation에 관련 Exception handling 처리할것


    // 최초로 reservation을 생성함

    @PostMapping("/reviews")
    public ResponseEntity<CreateReview.Response> createReview(
            @RequestBody @Valid CreateReview.Request request) {
        log.info("Post controller start  for  review creation : "
                + request.getReservationId());

        ReviewDto reviewDto = reviewService.createReview(
                request.getUserId(),
                request.getRating(),
                request.getReservationId(),
                request.getReviewContents()
        );

        return ResponseEntity.ok(CreateReview.Response.fromDto(reviewDto));
    }

    // 사용자에 의해서 수정
    @PatchMapping("/reviews/revise")
    public ResponseEntity<UpdateReview.Response> updateReview(
            @RequestBody @Valid UpdateReview.Request request) {
        log.info("Review update request received for reviewId: {}", request.getReviewId());
        ReviewDto reviewDto = reviewService.updateReview(
                request.getReviewId(),
                request.getRating(),
                request.getReviewContents());
        return ResponseEntity.ok(UpdateReview.Response.fromDto(reviewDto));
    }

    @PatchMapping("/reviews/delete")
    public ResponseEntity<DeleteReview.Response> deleteReview(
            @RequestBody @Valid DeleteReview.Request request) {
        log.info("Delete request received for reviewId: {}", request.getReviewId());
        return ResponseEntity.ok(DeleteReview.Response.fromDTO(
                reviewService.deleteReview(request.getReviewId())));

    }
}





