package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.ReviewDto;
import com.zerobase.zerobasereservation.entity.ReservationEntity;
import com.zerobase.zerobasereservation.entity.ReviewEntity;
import com.zerobase.zerobasereservation.entity.StoreEntity;
import com.zerobase.zerobasereservation.entity.UserEntity;
import com.zerobase.zerobasereservation.exception.CustomException;
import com.zerobase.zerobasereservation.repository.ReservationRepository;
import com.zerobase.zerobasereservation.repository.ReviewRepository;
import com.zerobase.zerobasereservation.repository.StoreRepository;
import com.zerobase.zerobasereservation.repository.UserRepository;
import com.zerobase.zerobasereservation.type.ErrorCode;
import com.zerobase.zerobasereservation.type.ReviewStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReviewService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final StoreService storeService;


    /*
    review의 필요정보를 받아서 review 생성하기,
    그 후 review의 store의 rating도 같이 업데이트 하기
     */
    public ReviewDto createReview(String userId, Integer rating, String reservationId, String reviewContents) {
        log.info("Creating review for user {} and reservation {}", userId, reservationId);

        ReservationEntity reservationEntity = reservationRepository.findByReservationId(reservationId).orElseThrow(
                () -> new CustomException(ErrorCode.RESERVATION_ID_NONEXISTENT)
        );

        UserEntity userEntity = userRepository.findByuserId(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_ID_NONEXISTENT)
        );

        StoreEntity storeEntity = reservationEntity.getStoreEntity();

        ReviewEntity reviewEntity = reviewRepository.save(ReviewEntity.builder()
                .reviewID(UUID.randomUUID().toString())
                .reservationEntity(reservationEntity)
                .userEntity(userEntity)
                .storeEntity(storeEntity)
                .rating(rating)
                .reviewContents(reviewContents)
                .reviewStatus(ReviewStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build());

        // Update the store's rating after the review CRUD
        storeService.updateRating(storeEntity);

        return ReviewDto.FromEntity(reviewEntity);
    }


    /*
    review의 필요정보를 받아서 review 수정하기,
    그 후 review의 store의 rating도 같이 업데이트 하기
     */

    public ReviewDto updateReview(String reviewId, Integer rating, String reviewContents) {
        log.info("Updating review for reviewId: {}", reviewId);

        ReviewEntity reviewEntity = reviewRepository.findByReviewID(reviewId).orElseThrow(
                () -> new CustomException(ErrorCode.REVIEW_NOT_FOUND)
        );

        reviewEntity.setRating(rating);
        reviewEntity.setReviewContents(reviewContents);
        reviewEntity.setUpdatedAt(LocalDateTime.now());


        // Update the store's rating after the review CRUD
        storeService.updateRating(reviewEntity.getStoreEntity());

        return ReviewDto.FromEntity(reviewRepository.save(reviewEntity));

    }

    /*
    reviw 삭제하기
    그 후 review의 store의 rating도 같이 업데이트 하기
     */


    public ReviewDto deleteReview(String reviewId) {
        log.info("Setting review status to DELETED for reviewId: {}", reviewId);

        ReviewEntity reviewEntity = reviewRepository.findByReviewID(reviewId).orElseThrow(
                () -> new CustomException(ErrorCode.REVIEW_NOT_FOUND)
        );

        reviewEntity.setReviewStatus(ReviewStatus.DELETED);
        reviewEntity.setUpdatedAt(LocalDateTime.now());

        // Update the store's rating after the review CRUD
        storeService.updateRating(reviewEntity.getStoreEntity());

        return ReviewDto.FromEntity(reviewRepository.save(reviewEntity));
    }
}
