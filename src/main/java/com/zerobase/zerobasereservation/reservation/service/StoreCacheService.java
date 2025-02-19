package com.zerobase.zerobasereservation.reservation.service;
import com.zerobase.zerobasereservation.reservation.dto.StoreDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreCacheService {
    private static final String STORE_CACHE_PREFIX = "store:"; // Redis 키 prefix
    private static final long EXPIRATION_TIME = 3600; // 1시간 (LFU 적용)

    private final StringRedisTemplate redisTemplate;
    private final StoreService storeService;

    // ✅ Redis에서 조회 후, 없으면 DB에서 가져와 캐싱
    public StoreDto findByStoreId(String storeId) {
        String redisKey = STORE_CACHE_PREFIX + storeId;

        // 1️⃣ Redis에서 데이터 가져오기 (LFU 캐시 적용됨)
        String storeData = redisTemplate.opsForValue().get(redisKey);
        if (storeData != null) {
            log.info("Found store with id from redis {}", storeId);
            return StoreDto.fromJson(storeData); // JSON 변환
        }

        // 2️⃣ 캐시에 없으면 DB에서 조회 후 Redis에 저장
        log.info(" store new data to redis {}", storeId);
        StoreDto storeDto = storeService.findByStoreId(storeId);
        redisTemplate.opsForValue().set(redisKey, StoreDto.toJson(storeDto), EXPIRATION_TIME, TimeUnit.SECONDS);

        return storeDto;
    }
}
