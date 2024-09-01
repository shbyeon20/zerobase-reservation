package com.zerobase.zerobasereservation.repository;

import com.zerobase.zerobasereservation.entity.ReservationEntity;
import com.zerobase.zerobasereservation.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long >
{
    Optional<ReviewEntity> findByReviewID(String reviewId);
}
