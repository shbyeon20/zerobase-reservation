package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.ReservationDto;
import com.zerobase.zerobasereservation.entity.PartnerEntity;
import com.zerobase.zerobasereservation.entity.ReservationEntity;
import com.zerobase.zerobasereservation.entity.StoreEntity;
import com.zerobase.zerobasereservation.entity.UserEntity;
import com.zerobase.zerobasereservation.exception.CustomException;
import com.zerobase.zerobasereservation.repository.ReservationRepository;
import com.zerobase.zerobasereservation.repository.StoreRepository;
import com.zerobase.zerobasereservation.repository.UserRepository;
import com.zerobase.zerobasereservation.type.ErrorCode;
import com.zerobase.zerobasereservation.type.ReservationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    // Test for createReservation method
    @Test
    void createReservation_Success() {
        // Given input 1
        String userId = "user123";
        String storeId = "store123";
        LocalDateTime reservationTime = LocalDateTime.now().plusDays(1);

        // given input 2
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userId(userId)
                .userName(userId)
                .phoneNumber("1234567890")
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .id(1L)
                .storeId(storeId)
                .build();

        given(userRepository.findByuserId(userId))
                .willReturn(Optional.of(userEntity));

        given(storeRepository.findBystoreId(storeId))
                .willReturn(Optional.of(storeEntity));


        // given input 3
        ReservationEntity savedReservation = ReservationEntity.builder()
                .id(1L)
                .reservationId("abcd1234")
                .userEntity(userEntity)
                .storeEntity(storeEntity)
                .reservationStatus(ReservationStatus.REQUESTED)
                .reservationTime(reservationTime)
                .createdAt(LocalDateTime.now())
                .build();


        given(reservationRepository.save(any(ReservationEntity.class)))
                .willReturn(savedReservation);

        // When
        ReservationDto result = reservationService.createReservation(userId, storeId, reservationTime);

        // Then compare given 1, 2 and 3
        assertNotNull(result);
        assertEquals("abcd1234", result.getReservationId());
        assertEquals(userId, result.getUserID());
        assertEquals(storeId, result.getStoreId());
        assertEquals(ReservationStatus.REQUESTED, result.getStatus());
        assertEquals(reservationTime, result.getReservationTime());

        // Capture the ReservationEntity passed to save
        ArgumentCaptor<ReservationEntity> reservationCaptor = ArgumentCaptor.forClass(ReservationEntity.class);
        verify(reservationRepository, times(1)).save(reservationCaptor.capture());

        ReservationEntity capturedReservation = reservationCaptor.getValue();
        assertSame(userEntity, capturedReservation.getUserEntity());
        assertSame(storeEntity, capturedReservation.getStoreEntity());
        assertEquals(ReservationStatus.REQUESTED, capturedReservation.getReservationStatus());
        assertEquals(reservationTime, capturedReservation.getReservationTime());

        // Verify that the userRepository and storeRepository methods are called with the correct parameters
        verify(userRepository, times(1)).findByuserId(userId);
        verify(storeRepository, times(1)).findBystoreId(storeId);
    }

    @Test
    void createReservation_UserNotFound() {
        // Given
        String userId = "user123";
        String storeId = "store123";
        LocalDateTime reservationTime = LocalDateTime.now().plusDays(1);

        given(userRepository.findByuserId(userId))
                .willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reservationService.createReservation(userId, storeId, reservationTime)
        );

        // Then
        assertEquals(ErrorCode.USERID_NONEXISTENT, exception.getErrorCode());
        assertEquals("partnerId not existing : " + userId, exception.getMessage());
        verify(reservationRepository, never()).save(any(ReservationEntity.class));
    }

    @Test
    void createReservation_StoreNotFound() {
        // Given1
        String userId = "user123";
        String storeId = "store123";
        LocalDateTime reservationTime = LocalDateTime.now().plusDays(1);

        // given2
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userId(userId)
                .userName("Test User")
                .phoneNumber("1234567890")
                .build();

        given(userRepository.findByuserId(userId))
                .willReturn(Optional.of(userEntity));

        given(storeRepository.findBystoreId(storeId))
                .willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reservationService.createReservation(userId, storeId, reservationTime)
        );

        // Then
        assertEquals(ErrorCode.STOREID_NONEXISTENT, exception.getErrorCode());
        assertEquals("partnerId not existing : " + storeId, exception.getMessage());
        verify(reservationRepository, never()).save(any(ReservationEntity.class));
    }

    // Test for searchReservationsByUser method
    @Test
    void searchReservationsByUser_Success() {
        // Given
        String userId = "user123";
        String storeId = "store123";

        StoreEntity storeEntity = StoreEntity.builder().storeId(storeId).build();
        UserEntity userEntity = UserEntity.builder().userId(userId).build();

        ReservationEntity reservation1 = ReservationEntity.builder()
                .reservationId("resv1")
                .userEntity(userEntity)
                .storeEntity(storeEntity)
                .reservationTime(LocalDateTime.now().plusDays(1))
                .reservationStatus(ReservationStatus.REQUESTED)
                .build();

        ReservationEntity reservation2 = ReservationEntity.builder()
                .reservationId("resv2")
                .userEntity(userEntity)
                .storeEntity(storeEntity)
                .reservationTime(LocalDateTime.now().plusDays(2))
                .reservationStatus(ReservationStatus.ACCEPTED)
                .build();

        List<ReservationEntity> reservations = Arrays.asList(reservation1, reservation2);

        given(reservationRepository.findAllByUserEntity_UserIdAndStoreEntity_StoreIdOrderByReservationTime(userId, storeId))
                .willReturn(reservations);

        // When
        List<ReservationDto> result = reservationService.searchReservationsByUser(userId, storeId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("resv1", result.get(0).getReservationId());
        assertEquals("resv2", result.get(1).getReservationId());
    }

    // Test for getReservationsByPartner method
    @Test
    void getReservationsByPartner_Success() {
        // Given
        String partnerId = "partner123";
        String storeId = "store123";

        PartnerEntity partnerEntity = PartnerEntity.builder()
                .partnerId(partnerId)
                .build();


        StoreEntity storeEntity = StoreEntity.builder()
                .id(1L)
                .storeId(storeId)
                .partnerEntity(partnerEntity)
                .build();

        UserEntity userEntity = mock(UserEntity.class);

        given(storeRepository.findBystoreId(storeId))
                .willReturn(Optional.of(storeEntity));

        ReservationEntity reservation1 = ReservationEntity.builder()
                .reservationId("resv1")
                .userEntity(userEntity)
                .reservationTime(LocalDateTime.now().plusDays(1))
                .reservationStatus(ReservationStatus.REQUESTED)
                .storeEntity(storeEntity)
                .build();

        ReservationEntity reservation2 = ReservationEntity.builder()
                .reservationId("resv2")
                .userEntity(userEntity)
                .reservationTime(LocalDateTime.now().plusDays(2))
                .reservationStatus(ReservationStatus.ACCEPTED)
                .storeEntity(storeEntity)
                .build();

        List<ReservationEntity> reservations = Arrays.asList(reservation1, reservation2);


        given(reservationRepository.findAllByStoreEntity_StoreIdOrderByReservationTime(storeId))
                .willReturn(reservations);

        // When
        List<ReservationDto> result = reservationService.getReservationsByPartner(partnerId, storeId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("resv1", result.get(0).getReservationId());
        assertEquals("resv2", result.get(1).getReservationId());
    }

    @Test
    void getReservationsByPartner_StoreNotFound() {
        // Given
        String partnerId = "partner123";
        String storeId = "store123";

        given(storeRepository.findBystoreId(storeId))
                .willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reservationService.getReservationsByPartner(partnerId, storeId)
        );

        // Then
        assertEquals(ErrorCode.STOREID_NONEXISTENT, exception.getErrorCode());
    }

    @Test
    void getReservationsByPartner_PartnerIdMismatch() {
        // Given
        String partnerId = "partner123";
        String storeId = "store123";

        PartnerEntity otherPartner = PartnerEntity.builder()
                .partnerId("otherPartner")
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .storeId(storeId)
                .partnerEntity(otherPartner)
                .build();

        given(storeRepository.findBystoreId(storeId))
                .willReturn(Optional.of(storeEntity));

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reservationService.getReservationsByPartner(partnerId, storeId)
        );

        // Then
        assertEquals(ErrorCode.PARTNERID_NONEXISTENT, exception.getErrorCode());
    }

    // Test for acceptReservation method
    @Test
    void acceptReservation_Success() {
        // Given param
        String partnerId = "partner123";
        String reservationId = "resv123";

        // given entities from db
        PartnerEntity partnerEntity = PartnerEntity.builder()
                .partnerId(partnerId)
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .partnerEntity(partnerEntity)
                .build();

        UserEntity userEntity = UserEntity.builder()
                .userId("user123").build();

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .reservationId(reservationId)
                .userEntity(userEntity)
                .reservationStatus(ReservationStatus.REQUESTED)
                .storeEntity(storeEntity)
                .build();

        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.of(reservationEntity));

        given(reservationRepository.save(reservationEntity))
                .willReturn(reservationEntity);

        // When
        ReservationDto result = reservationService.acceptReservation(partnerId, reservationId);

        // Then
        assertNotNull(result);
        assertEquals(ReservationStatus.ACCEPTED, result.getStatus());
        verify(reservationRepository, times(1)).findByReservationId(reservationId);

        // Verify that the reservation status was updated
        assertEquals(ReservationStatus.ACCEPTED, reservationEntity.getReservationStatus());
        verify(reservationRepository).save(reservationEntity);
    }

    @Test
    void acceptReservation_ReservationNotFound() {
        // Given
        String partnerId = "partner123";
        String reservationId = "resv123";

        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reservationService.acceptReservation(partnerId, reservationId)
        );

        // Then
        assertEquals(ErrorCode.RESERVATION_ID_NONEXISTENT, exception.getErrorCode());
    }

    @Test
    void acceptReservation_PartnerIdMismatch() {
        // Given
        String partnerId = "partner123";
        String reservationId = "resv123";

        PartnerEntity otherPartner = PartnerEntity.builder()
                .partnerId("otherPartner")
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .partnerEntity(otherPartner)
                .build();

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .reservationId(reservationId)
                .reservationStatus(ReservationStatus.REQUESTED)
                .storeEntity(storeEntity)
                .build();

        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.of(reservationEntity));

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reservationService.acceptReservation(partnerId, reservationId)
        );

        // Then
        assertEquals(ErrorCode.MEMBERID_STOREOWNER_UNMATCHED, exception.getErrorCode());
    }

    @Test
    void acceptReservation_InvalidStatus() {
        // Given
        String partnerId = "partner123";
        String reservationId = "resv123";

        PartnerEntity partnerEntity = PartnerEntity.builder()
                .partnerId(partnerId)
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .partnerEntity(partnerEntity)
                .build();

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .reservationId(reservationId)
                .reservationStatus(ReservationStatus.ACCEPTED) // Already accepted
                .storeEntity(storeEntity)
                .build();

        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.of(reservationEntity));

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reservationService.acceptReservation(partnerId, reservationId)
        );

        // Then
        assertEquals(ErrorCode.RESERVATION_STATUS_ERROR, exception.getErrorCode());
    }

    // Test for confirmReservation method
    @Test
    void confirmReservation_Success() {
        // Given param
        String reservationId = "resv123";

        // mock entity

        UserEntity userEntity = UserEntity.builder()
                .userId("user123")
                .build();

        StoreEntity storeEntity = StoreEntity.builder()
                .storeId("store123")
                .build();

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .reservationId(reservationId)
                .userEntity(userEntity)
                .storeEntity(storeEntity)
                .reservationStatus(ReservationStatus.ACCEPTED)
                .reservationTime(LocalDateTime.now().plusMinutes(20))
                .build();



        // mock dependency method
        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.of(reservationEntity));

        given(reservationRepository.save(reservationEntity))
                .willReturn(reservationEntity);

        // When
        ReservationDto result = reservationService.confirmReservation(reservationId);

        // Then
        assertNotNull(result);
        assertEquals(ReservationStatus.CONFIRMED, result.getStatus());

        // Verify that the reservation status was updated
        assertEquals(ReservationStatus.CONFIRMED, reservationEntity.getReservationStatus());
        verify(reservationRepository).save(reservationEntity);
    }

    @Test
    void confirmReservation_ReservationNotFound() {
        // Given
        String reservationId = "resv123";

        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reservationService.confirmReservation(reservationId)
        );

        // Then
        assertEquals(ErrorCode.RESERVATION_ID_NONEXISTENT, exception.getErrorCode());
    }

    @Test
    void confirmReservation_InvalidStatus() {
        // Given
        String reservationId = "resv123";

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .reservationId(reservationId)
                .reservationStatus(ReservationStatus.REQUESTED) // Not accepted yet
                .reservationTime(LocalDateTime.now().plusMinutes(20))
                .build();

        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.of(reservationEntity));

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reservationService.confirmReservation(reservationId)
        );

        // Then
        assertEquals(ErrorCode.RESERVATION_STATUS_ERROR, exception.getErrorCode());
    }

    @Test
    void confirmReservation_TooLate() {
        // Given
        String reservationId = "resv123";

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .reservationId(reservationId)
                .reservationStatus(ReservationStatus.ACCEPTED)
                .reservationTime(LocalDateTime.now().plusMinutes(5)) // Less than 10 minutes left
                .build();

        given(reservationRepository.findByReservationId(reservationId))
                .willReturn(Optional.of(reservationEntity));

        // When
        CustomException exception = assertThrows(CustomException.class, () ->
                reservationService.confirmReservation(reservationId)
        );

        // Then
        assertEquals(ErrorCode.CONFIRMATION_TOO_LATE, exception.getErrorCode());
    }
}
