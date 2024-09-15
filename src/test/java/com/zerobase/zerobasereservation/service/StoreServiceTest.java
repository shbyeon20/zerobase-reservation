package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.StoreDto;
import com.zerobase.zerobasereservation.entity.PartnerEntity;
import com.zerobase.zerobasereservation.entity.ReviewEntity;
import com.zerobase.zerobasereservation.entity.StoreEntity;
import com.zerobase.zerobasereservation.exception.CustomException;
import com.zerobase.zerobasereservation.repository.PartnerRepository;
import com.zerobase.zerobasereservation.repository.ReviewRepository;
import com.zerobase.zerobasereservation.repository.StoreRepository;
import com.zerobase.zerobasereservation.type.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private StoreService storeService;

    private PartnerEntity partnerEntity;
    private StoreEntity storeEntity;

    @BeforeEach
    void setUp() {
        partnerEntity = PartnerEntity.builder()
                .partnerId("partner123")
                .partnerName("Test Partner")
                .build();

        storeEntity = StoreEntity.builder()
                .partnerEntity(partnerEntity)
                .storeId("store123")
                .address("123 Main St")
                .storeComment("Test Store")
                .registeredAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createStore_ShouldCreateStoreSuccessfully() {
        // Given
        String partnerId = "partner123";
        String storeId = "store123";
        String address = "123 Main St";
        String storeComment = "Test Store";

        given(partnerRepository.findBypartnerId(partnerId))
                .willReturn(Optional.of(partnerEntity));

        given(storeRepository.save(any(StoreEntity.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // When
        StoreDto result = storeService.createStore(partnerId, storeId, address, storeComment);

        // Then
        then(partnerRepository).should().findBypartnerId(partnerId);
        then(storeRepository).should().save(any(StoreEntity.class));

        assertEquals(storeId, result.getStoreId());
        assertEquals(address, result.getAddress());
        assertEquals(partnerId, result.getPartnerId());
        assertEquals(storeComment, result.getStoreComment());
    }

    @Test
    void createStore_ShouldThrowException_WhenPartnerNotFound() {
        // Given
        String partnerId = "nonexistentPartnerId";
        String storeId = "store123";
        String address = "123 Main St";
        String storeComment = "Test Store";

        given(partnerRepository.findBypartnerId(partnerId))
                .willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            storeService.createStore(partnerId, storeId, address, storeComment);
        });

        // Then
        assertEquals(ErrorCode.PARTNERID_NONEXISTENT, exception.getErrorCode());
    }

    @Test
    void findByPartnerId_ShouldReturnListOfStores() {
        // Given
        String partnerId = "partner123";
        List<StoreEntity> storeEntities = Arrays.asList(storeEntity);

        given(partnerRepository.findBypartnerId(partnerId))
                .willReturn(Optional.of(partnerEntity));

        given(storeRepository.findAllByPartnerEntity(partnerEntity))
                .willReturn(storeEntities);

        // When
        List<StoreDto> result = storeService.findByPartnerId(partnerId);

        // Then
        then(partnerRepository).should().findBypartnerId(partnerId);
        then(storeRepository).should().findAllByPartnerEntity(partnerEntity);

        assertEquals(1, result.size());
        assertEquals(storeEntity.getStoreId(), result.get(0).getStoreId());
    }

    @Test
    void findByPartnerId_ShouldThrowException_WhenPartnerNotFound() {
        // Given
        String partnerId = "nonexistentPartnerId";

        given(partnerRepository.findBypartnerId(partnerId))
                .willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            storeService.findByPartnerId(partnerId);
        });

        // Then
        assertEquals(ErrorCode.PARTNERID_NONEXISTENT, exception.getErrorCode());
    }

    @Test
    void findByStoreId_ShouldReturnStore() {
        // Given
        String partnerId = "partner123";
        String storeId = "store123";

        given(storeRepository.findBystoreId(storeId))
                .willReturn(Optional.of(storeEntity));

        // When
        StoreDto result = storeService.findByStoreId(partnerId, storeId);

        // Then
        then(storeRepository).should().findBystoreId(storeId);

        assertEquals(storeId, result.getStoreId());
        assertEquals(partnerId, storeEntity.getPartnerEntity().getPartnerId());
    }

    @Test
    void findByStoreId_ShouldThrowException_WhenStoreNotFound() {
        // Given
        String partnerId = "partner123";
        String storeId = "nonexistentStoreId";

        given(storeRepository.findBystoreId(storeId))
                .willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            storeService.findByStoreId(partnerId, storeId);
        });

        // Then
        assertEquals(ErrorCode.STOREID_NONEXISTENT, exception.getErrorCode());
    }

    @Test
    void findByStoreId_ShouldThrowException_WhenPartnerIdDoesNotMatch() {
        // Given
        String partnerId = "otherPartnerId";
        String storeId = "store123";

        given(storeRepository.findBystoreId(storeId))
                .willReturn(Optional.of(storeEntity));

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            storeService.findByStoreId(partnerId, storeId);
        });

        // Then
        assertEquals(ErrorCode.MEMBERID_STOREOWNER_UNMATCHED, exception.getErrorCode());
    }

    @Test
    void updateRating_ShouldUpdateStoreRating() {
        // Given
        Double newRating = 4.5;

        given(reviewRepository.reupdateAverageRating(storeEntity))
                .willReturn(newRating);

        given(storeRepository.save(storeEntity))
                .willReturn(storeEntity);

        // When
        storeService.updateRating(storeEntity);

        // Then
        then(reviewRepository).should().reupdateAverageRating(storeEntity);
        then(storeRepository).should().save(storeEntity);

        assertEquals(newRating, storeEntity.getRating());
    }
}
