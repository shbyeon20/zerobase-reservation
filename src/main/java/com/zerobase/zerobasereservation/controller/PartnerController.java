package com.zerobase.zerobasereservation.controller;

import com.zerobase.zerobasereservation.dto.CreatePartner;
import com.zerobase.zerobasereservation.dto.PartnerDto;
import com.zerobase.zerobasereservation.service.PartnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PartnerController {
    private final PartnerService partnerService;

    //todo : validation에 관련 Exception handling 처리할것

    @PostMapping("/partners")
    public ResponseEntity<CreatePartner.Response> createPartner(
            @RequestBody @Valid CreatePartner.Request request) {
        log.info("Creating partner : {}", request);

        PartnerDto partner = partnerService.createPartner(
                request.getPartnerId(),
                request.getPartnerName(),
                request.getBusinessId()
        );

        return ResponseEntity.ok().body(
                CreatePartner.Response.fromDto(partner));
    }


}
