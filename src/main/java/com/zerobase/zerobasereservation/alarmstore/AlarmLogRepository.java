package com.zerobase.zerobasereservation.alarmstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmLogRepository extends JpaRepository<AlarmLogEntity, Long> {

}
