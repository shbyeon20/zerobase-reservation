package com.zerobase.zerobasereservation.controller;

import com.zerobase.zerobasereservation.dto.CreateReview;
import com.zerobase.zerobasereservation.dto.DeleteReview;
import com.zerobase.zerobasereservation.dto.ReviewDto;
import com.zerobase.zerobasereservation.exception.UpdateReview;
import com.zerobase.zerobasereservation.security.JwtHandler;
import com.zerobase.zerobasereservation.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtHandler jwtHandler;



    /*
    * user 는 Confirmed 된 reservationId 를 mapping 하여 review 를 만든다
     */
    @PostMapping("/user/create")
    public ResponseEntity<CreateReview.Response> createReview(
            @RequestBody @Valid CreateReview.Request request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        log.info("Post controller start  for  review creation : {}", request.getReservationId());

        ReviewDto reviewDto = reviewService.createReview(
                memberId,
                request.getRating(),
                request.getReservationId(),
                request.getReviewContents()
        );

        return ResponseEntity.ok(CreateReview.Response.fromDto(reviewDto));
    }
    /*
    *user 는 생성된 reviewId를 통해서 자신이 생성한 리뷰에 한하여 수정한다.
     */


    @PatchMapping("/user/revise")
    public ResponseEntity<UpdateReview.Response> updateReview(
            @RequestBody @Valid UpdateReview.Request request
            ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        log.info("Review update request received for reviewId: {}", request.getReviewId());
        ReviewDto reviewDto = reviewService.updateReview(
                memberId,
                request.getReviewId(),
                request.getRating(),
                request.getReviewContents());
        return ResponseEntity.ok(UpdateReview.Response.fromDto(reviewDto));
    }



    @PatchMapping("/delete")
    public ResponseEntity<DeleteReview.Response> deleteReview(
            @RequestBody @Valid DeleteReview.Request request) {
        log.info("Delete request received for reviewId: {}", request.getReviewId());
        return ResponseEntity.ok(DeleteReview.Response.fromDTO(
                reviewService.deleteReview(
                        request.getMemberId(), request.getReviewId())));

    }
}








