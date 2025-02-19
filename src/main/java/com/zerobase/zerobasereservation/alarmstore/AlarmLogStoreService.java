package com.zerobase.zerobasereservation.alarmstore;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmLogStoreService {

    private final AlarmLogRepository alarmLogRepository;


    public void save(String memberId, String message) {
        AlarmLogEntity entity = AlarmLogEntity.builder().memberId(memberId)
            .message(message).build();

        alarmLogRepository.save(entity);

    }
}
