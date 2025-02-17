package com.zerobase.zerobasereservation.reservation.controller;


import com.zerobase.zerobasereservation.reservation.dto.CreatePartner;
import com.zerobase.zerobasereservation.reservation.dto.CreateUser;
import com.zerobase.zerobasereservation.reservation.dto.PartnerDto;
import com.zerobase.zerobasereservation.reservation.dto.SignAuth;
import com.zerobase.zerobasereservation.reservation.dto.UserDto;
import com.zerobase.zerobasereservation.reservation.security.AuthServiceInterface;
import com.zerobase.zerobasereservation.reservation.service.PartnerDataManager;
import com.zerobase.zerobasereservation.reservation.service.UserDataManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserDataManager userDataManager;
    private final PartnerDataManager partnerDataManager;
    private final AuthServiceInterface authServiceInterface;


    /*
     memberEntity 와 userEntity 최초 생성함.
     memberEntity 는 보안기능을 위해 후속 추가되어  userService 내에서
     memberService 를 호출하여 Transaction 관리함.

     */

    @PostMapping("/partner")
    public ResponseEntity<CreatePartner.Response> createPartner(
            @RequestBody @Valid CreatePartner.Request request) {
        log.info("Creating partner : {}", request);



        PartnerDto partnerDto = partnerDataManager.createPartner
                (       request.getPartnerId(),
                        request.getPassword(),
                        request.getPartnerName(),
                        request.getBusinessId(),
                        request.getPhoneNumber()
                );

        return ResponseEntity.ok().body(
                CreatePartner.Response.fromDto(partnerDto));
    }


    /*
        memberEntity 와 userEntity 최초 생성함.
        memberEntity 는 보안기능을 위해 후속 추가되어  userService 내에서
        memberService 를 호출하여 Transaction 관리함.
     */

    @PostMapping("/user")
    public ResponseEntity<CreateUser.Response> createUser(
            @RequestBody @Valid CreateUser.Request request) {

        log.info("Creating user request received : {}", request.getUserId());

        UserDto userDto = userDataManager.createUser(
                request.getUserId(),
                request.getPassword(),
                request.getUserName(),
                request.getPhoneNumber()

        );

        return ResponseEntity.ok().body(
                CreateUser.Response.fromDto(userDto));
    }


    /*
    Dao Authentication 을 행하고 결과값으로 JWT token 을 받음
     */
    @GetMapping("/sign-in")
    public ResponseEntity<String> signIn(
            @RequestBody @Valid SignAuth.SignIn signIn) {

        String token =  authServiceInterface.getKey(signIn.getId(), signIn.getPassword());

        return ResponseEntity.ok(token);
    }


}
