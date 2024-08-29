package com.zerobase.zerobasereservation.controller;

import com.zerobase.zerobasereservation.dto.CreatePartner;
import com.zerobase.zerobasereservation.dto.CreateUser;
import com.zerobase.zerobasereservation.dto.PartnerDto;
import com.zerobase.zerobasereservation.service.PartnerService;
import com.zerobase.zerobasereservation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //todo : validation에 관련 Exception handling 처리할것

    @PostMapping("/users")
    public ResponseEntity<CreateUser.Response> createUser(
            @RequestBody @Valid CreateUser.Request request) {
        log.info("Creating user : {}", request);

        PartnerDto partner = userService.createUser(
                request.getPartnerId(),
                request.getPartnerName(),
                request.getBusinessId()
        );

        return ResponseEntity.ok().body(
                CreateUser.Response.fromDto(partner));
    }


}
