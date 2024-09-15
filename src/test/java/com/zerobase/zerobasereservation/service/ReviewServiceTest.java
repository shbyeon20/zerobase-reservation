package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.ReviewDto;
import com.zerobase.zerobasereservation.entity.*;
import com.zerobase.zerobasereservation.exception.CustomException;
import com.zerobase.zerobasereservation.repository.ReservationRepository;
import com.zerobase.zerobasereservation.repository.ReviewRepository;
import com.zerobase.zerobasereservation.repository.UserRepository;
import com.zerobase.zerobasereservation.type.ErrorCode;
import com.zerobase.zerobasereservation.type.ReviewStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    ReservationRepository reservationRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    StoreService storeService;
    @InjectMocks
    ReviewService reviewService;

    @Test
    void createReview_Success() {

        //given param

        String userId = "123";
        Integer rating = 3;
        String reservationId = "456";
        String reviewContent = "Review content";

        // given entities

        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .build();

        StoreEntity storeEntity = mock(StoreEntity.class);

        ReservationEntity reservationEntity =
                ReservationEntity.builder()
                        .reservationId(reservationId)
                        .storeEntity(storeEntity)
                        .userEntity(userEntity)
                        .build();

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .reviewID("1234")
                .reservationEntity(reservationEntity)
                .userEntity(userEntity)
                .storeEntity(storeEntity)
                .rating(rating)
                .reviewContents(reviewContent)
                .reviewStatus(ReviewStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        // given mocks
        given(reservationRepository.findByReservationId(anyString()))
                .willReturn(Optional.of(reservationEntity));

        given(userRepository.findByuserId(anyString()))
                .willReturn(Optional.of(userEntity));

        given(reviewRepository.save(any(ReviewEntity.class)))
                .willReturn(reviewEntity);


        //when

        ReviewDto review = reviewService.createReview(userId, rating, reservationId, reviewContent);

        //then

        assertNotNull(review);
        assertEquals(reservationId, review.getReservationId());
        assertEquals(userId, review.getUserId());
        assertEquals(rating, review.getRating());
        assertEquals(reviewContent, review.getReviewContents());

        // Verify that the repositories are called correctly
        verify(userRepository, times(1)).findByuserId(userId);
        verify(reservationRepository, times(1)).findByReservationId(reservationId);
        verify(reviewRepository, times(1)).save(any(ReviewEntity.class));
        verify(storeService, times(1)).updateRating(any(StoreEntity.class));

    }

    @Test
    void creatReview_ReservationNotFound() {
        // Given param
        String userId = "user123";
        String storeId = "store123";
        String reservationId = "456";
        String content = "Great service!";
        int rating = 5;

        // given mocking
        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.empty());


        //when

        CustomException customException = assertThrows(CustomException.class,
                () -> reviewService.createReview(userId, rating, reservationId, content));


        //then

        assertEquals(customException.getErrorCode(), ErrorCode.RESERVATION_ID_NONEXISTENT);
        verify(reservationRepository, times(1)).findByReservationId(anyString());
        verify(userRepository, times(0)).findByuserId(anyString());
        verify(reviewRepository, times(0)).save(any(ReviewEntity.class));
        verify(userRepository, never()).findByuserId(anyString());
        verify(reviewRepository, never()).save(any(ReviewEntity.class));


    }

    @Test
    void createReview_UserNotFound() {
        // Given parameters
        String userId = "user123";
        Integer rating = 5;
        String reservationId = "456";
        String reviewContent = "Great service!";

        // Given entities
        StoreEntity storeEntity = StoreEntity.builder()
                .storeId("store123")
                .build();

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .reservationId(reservationId)
                .userEntity(UserEntity.builder().userId(userId).build()) //
                .storeEntity(storeEntity)
                .build();

        // Mocking methods
        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.of(reservationEntity));

        given(userRepository.findByuserId(userId))
                .willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reviewService.createReview(userId, rating, reservationId, reviewContent)
        );

        // Then
        assertEquals(ErrorCode.USERID_NONEXISTENT, exception.getErrorCode());
        verify(userRepository, times(1)).findByuserId(userId);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
    }

    @Test
    void createReview_UserMismatch() {
        // Given parameters
        String userId = "user123";
        Integer rating = 5;
        String reservationId = "456";
        String reviewContent = "Great service!";

        // Given entities
        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .build();

        UserEntity otherUser = UserEntity.builder()
                .userId("otherUser")
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .storeId("store123")
                .build();

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .reservationId(reservationId)
                .userEntity(otherUser) // Reservation belongs to a different user
                .storeEntity(storeEntity)
                .build();

        // Mocking methods
        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.of(reservationEntity));


        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reviewService.createReview(userId, rating, reservationId, reviewContent)
        );

        // Then
        assertEquals(ErrorCode.USERID_REVIEWUSER_UNMATCHED, exception.getErrorCode());
        verify(userRepository, never()).findByuserId(userId);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
    }


    @Test
    void updateReview_Success() {
        // Given parameters
        String userId = "user123";
        String reviewId = "review456";
        Integer newRating = 4;
        String newReviewContents = "Updated review content";

        // Given entities
        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .storeId("store789")
                .build();

        ReviewEntity existingReviewEntity = ReviewEntity.builder()
                .reviewID(reviewId)
                .reviewStatus(ReviewStatus.ACTIVE)
                .userEntity(userEntity)
                .reservationEntity(mock(ReservationEntity.class))
                .storeEntity(storeEntity)
                .rating(5)
                .reviewContents("Original review content")
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        // Mocking methods
        given(reviewRepository.findByReviewID(reviewId))
                .willReturn(Optional.of(existingReviewEntity));

        given(reviewRepository.save(any(ReviewEntity.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // When
        ReviewDto result = reviewService.updateReview(userId, reviewId, newRating, newReviewContents);

        // Then
        assertNotNull(result);
        assertEquals(reviewId, result.getReviewId());
        assertEquals(newRating, result.getRating());
        assertEquals(newReviewContents, result.getReviewContents());

        // Verify interactions
        verify(reviewRepository, times(1)).findByReviewID(reviewId);
        verify(reviewRepository, times(1)).save(existingReviewEntity);
        verify(storeService, times(1)).updateRating(storeEntity);
    }

    @Test
    void updateReview_ReviewNotFound() {
        // Given parameters
        String userId = "user123";
        String reviewId = "review456";
        Integer newRating = 4;
        String newReviewContents = "Updated review content";

        // Mocking methods
        given(reviewRepository.findByReviewID(reviewId))
                .willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reviewService.updateReview(userId, reviewId, newRating, newReviewContents)
        );

        // Then
        assertEquals(ErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
        verify(reviewRepository, times(1)).findByReviewID(reviewId);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
        verify(storeService, never()).updateRating(any(StoreEntity.class));
    }

    @Test
    void updateReview_UserMismatch() {
        // Given parameters
        String userId = "user123";
        String reviewId = "review456";
        Integer newRating = 4;
        String newReviewContents = "Updated review content";

        // Given entities
        UserEntity reviewOwner = UserEntity.builder()
                .userId("otherUser")
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .storeId("store789")
                .build();

        ReviewEntity existingReviewEntity = ReviewEntity.builder()
                .reviewID(reviewId)
                .userEntity(reviewOwner)
                .storeEntity(storeEntity)
                .rating(5)
                .reviewContents("Original review content")
                .reviewStatus(ReviewStatus.ACTIVE)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        // Mocking methods
        given(reviewRepository.findByReviewID(reviewId))
                .willReturn(Optional.of(existingReviewEntity));

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reviewService.updateReview(userId, reviewId, newRating, newReviewContents)
        );

        // Then
        assertEquals(ErrorCode.USERID_REVIEWUSER_UNMATCHED, exception.getErrorCode());
        verify(reviewRepository, times(1)).findByReviewID(reviewId);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
        verify(storeService, never()).updateRating(any(StoreEntity.class));
    }

    // Test cases for deleteReview method

    @Test
    void deleteReview_Success() {
        // Given parameters
        String memberId = "user123";
        String reviewId = "review456";

        // Given entities
        UserEntity userEntity = UserEntity.builder()
                .userId(memberId)
                .build();

        PartnerEntity partnerEntity = PartnerEntity.builder()
                .partnerId(memberId)
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .storeId("store789")
                .partnerEntity(partnerEntity)
                .build();

        ReservationEntity reservationEntity = mock(ReservationEntity.class);

        ReviewEntity existingReviewEntity = ReviewEntity.builder()
                .reviewID(reviewId)
                .reservationEntity(reservationEntity)
                .userEntity(userEntity)
                .storeEntity(storeEntity)
                .reviewStatus(ReviewStatus.ACTIVE)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        ReviewEntity changedReviewEntity = ReviewEntity.builder()
                .reviewID(reviewId)
                .reservationEntity(reservationEntity)
                .userEntity(userEntity)
                .storeEntity(storeEntity)
                .reviewStatus(ReviewStatus.DELETED)
                .createdAt(LocalDateTime.now())
                .build();

        // Mocking methods
        given(reviewRepository.findByReviewID(reviewId))
                .willReturn(Optional.of(existingReviewEntity));

        given(reviewRepository.save(any(ReviewEntity.class)))
                .willReturn(changedReviewEntity);

        // When
        ReviewDto result = reviewService.deleteReview(memberId, reviewId);

        // Then
        assertNotNull(result);
        assertEquals(ReviewStatus.DELETED, result.getStatus());

        // Verify interactions
        verify(reviewRepository, times(1)).findByReviewID(reviewId);
        verify(reviewRepository, times(1)).save(existingReviewEntity);
        verify(storeService, times(1)).updateRating(storeEntity);
    }

    @Test
    void deleteReview_ReviewNotFound() {
        // Given parameters
        String memberId = "user123";
        String reviewId = "review456";

        // Mocking methods
        given(reviewRepository.findByReviewID(reviewId))
                .willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reviewService.deleteReview(memberId, reviewId)
        );

        // Then
        assertEquals(ErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
        verify(reviewRepository, times(1)).findByReviewID(reviewId);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
        verify(storeService, never()).updateRating(any(StoreEntity.class));
    }

    @Test
    void deleteReview_UserMismatch() {
        // Given parameters
        String memberId = "user123";
        String reviewId = "review456";

        // Given entities
        UserEntity reviewOwner = UserEntity.builder()
                .userId("otherUser")
                .build();

        PartnerEntity partnerEntity = PartnerEntity.builder()
                .partnerId("otherPartner")
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .storeId("store789")
                .partnerEntity(partnerEntity)
                .build();

        ReviewEntity existingReviewEntity = ReviewEntity.builder()
                .reviewID(reviewId)
                .userEntity(reviewOwner)
                .storeEntity(storeEntity)
                .reviewStatus(ReviewStatus.ACTIVE)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        // Mocking methods
        given(reviewRepository.findByReviewID(reviewId))
                .willReturn(Optional.of(existingReviewEntity));

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reviewService.deleteReview(memberId, reviewId)
        );

        // Then
        assertEquals(ErrorCode.MEMBERID_REVIEWUSER_UNMATCHED, exception.getErrorCode());
        verify(reviewRepository, times(1)).findByReviewID(reviewId);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
        verify(storeService, never()).updateRating(any(StoreEntity.class));
    }

    @Test
    void deleteReview_PartnerMismatch() {
        // Given parameters
        String memberId = "partner123";
        String reviewId = "review456";

        // Given entities
        UserEntity reviewOwner = UserEntity.builder()
                .userId("otherUser")
                .build();

        PartnerEntity partnerEntity = PartnerEntity.builder()
                .partnerId("otherPartner")
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .storeId("store789")
                .partnerEntity(partnerEntity)
                .build();

        ReviewEntity existingReviewEntity = ReviewEntity.builder()
                .reviewID(reviewId)
                .userEntity(reviewOwner)
                .storeEntity(storeEntity)
                .reviewStatus(ReviewStatus.ACTIVE)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        // Mocking methods
        given(reviewRepository.findByReviewID(reviewId))
                .willReturn(Optional.of(existingReviewEntity));

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reviewService.deleteReview(memberId, reviewId)
        );

        // Then
        assertEquals(ErrorCode.MEMBERID_REVIEWUSER_UNMATCHED, exception.getErrorCode());
        verify(reviewRepository, times(1)).findByReviewID(reviewId);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
        verify(storeService, never()).updateRating(any(StoreEntity.class));
    }

    // Additional test cases for edge conditions

    @Test
    void createReview_ReservationUserMismatch() {
        // Given parameters
        String userId = "user123";
        Integer rating = 5;
        String reservationId = "456";
        String reviewContent = "Great service!";

        // Given entities
        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .build();

        UserEntity reservationUser = UserEntity.builder()
                .userId("otherUser")
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .storeId("store789")
                .build();

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .reservationId(reservationId)
                .userEntity(reservationUser)
                .storeEntity(storeEntity)
                .build();

        // Mocking methods
        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.of(reservationEntity));

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reviewService.createReview(userId, rating, reservationId, reviewContent)
        );

        // Then
        assertEquals(ErrorCode.USERID_REVIEWUSER_UNMATCHED, exception.getErrorCode());
        verify(reservationRepository, times(1)).findByReservationId(reservationId);
        verify(userRepository, never()).findByuserId(userId);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
    }
}