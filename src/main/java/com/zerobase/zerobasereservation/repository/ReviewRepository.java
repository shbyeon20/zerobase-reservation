package com.zerobase.zerobasereservation.repository;

import com.zerobase.zerobasereservation.entity.ReviewEntity;
import com.zerobase.zerobasereservation.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long >
{
    Optional<ReviewEntity> findByReviewID(String reviewId);

    // Custom query to calculate the average rating for a specific store
    @Query("SELECT ROUND(AVG(r.rating), 1) FROM ReviewEntity r WHERE " +
            "r.storeEntity = :storeEntity")
    Double findAverageRatingByStoreEntity(@Param("storeId") StoreEntity storeEntity);
}
