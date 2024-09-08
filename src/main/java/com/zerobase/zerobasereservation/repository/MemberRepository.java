package com.zerobase.zerobasereservation.repository;

import com.zerobase.zerobasereservation.entity.MemberDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberDetails, Long> {
    boolean existsByMemberId(String memberId);

    Optional<MemberDetails> findByMemberId(String memberId);
}
