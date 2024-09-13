package com.zerobase.zerobasereservation.service;

import com.zerobase.zerobasereservation.dto.PartnerDto;
import com.zerobase.zerobasereservation.entity.PartnerEntity;
import com.zerobase.zerobasereservation.repository.PartnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PartnerServiceTest {

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private MemberAuthService memberAuthService;

    @InjectMocks
    private PartnerService partnerService;

    @Test
    void createPartner_Success() {
        // Given
        String partnerId = "partner123";
        String password = "password";
        String partnerName = "Partner Name";
        String businessId = "BIZ123";
        String phoneNumber = "1234567890";

        PartnerEntity savedPartnerEntity = PartnerEntity.builder()
                .id(1L)
                .partnerId(partnerId)
                .partnerName(partnerName)
                .businessId(businessId)
                .phoneNumber(phoneNumber)
                .registeredAt(LocalDateTime.now())
                .build();

        given(partnerRepository.save(any(PartnerEntity.class)))
                .willReturn(savedPartnerEntity);

        // When
        PartnerDto result = partnerService.createPartner(partnerId, password, partnerName, businessId, phoneNumber);

        // Then
        assertNotNull(result);
        assertEquals(partnerId, result.getPartnerId());
        assertEquals(partnerName, result.getPartnerName());


        // Verify that memberAuthService.register was called
        verify(memberAuthService, times(1)).register("partnerId", password);

        // Capture the PartnerEntity saved to the repository
        ArgumentCaptor<PartnerEntity> captor = ArgumentCaptor.forClass(PartnerEntity.class);
        verify(partnerRepository, times(1)).save(captor.capture());

        PartnerEntity capturedEntity = captor.getValue();
        assertEquals(partnerId, capturedEntity.getPartnerId());
        assertEquals(partnerName, capturedEntity.getPartnerName());
        assertEquals(businessId, capturedEntity.getBusinessId());
        assertEquals(phoneNumber, capturedEntity.getPhoneNumber());
        assertNotNull(capturedEntity.getRegisteredAt());
    }

    @Test
    void createPartner_MemberRegistrationFails() {
        // Given
        String partnerId = "partner123";
        String password = "password";
        String partnerName = "Partner Name";
        String businessId = "BIZ123";
        String phoneNumber = "1234567890";

        // Simulate memberAuthService.register throwing an exception
        doThrow(new RuntimeException("Member registration failed"))
                .when(memberAuthService).register(partnerId, password);

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                partnerService.createPartner(partnerId, password, partnerName, businessId, phoneNumber)
        );

        // Then
        assertEquals("Member registration failed", exception.getMessage());

        // Verify that partnerRepository.save was never called
        verify(partnerRepository, never()).save(any(PartnerEntity.class));
    }
}
