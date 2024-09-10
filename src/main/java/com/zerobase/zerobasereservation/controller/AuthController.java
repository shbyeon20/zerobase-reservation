package com.zerobase.zerobasereservation.controller;


import com.zerobase.zerobasereservation.dto.*;
import com.zerobase.zerobasereservation.security.JwtHandler;
import com.zerobase.zerobasereservation.security.MemberAuthService;
import com.zerobase.zerobasereservation.service.PartnerService;
import com.zerobase.zerobasereservation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final MemberAuthService memberAuthService;
    private final JwtHandler jwtHandler;
    private final UserService userService;
    private final PartnerService partnerService;
    private final PasswordEncoder passwordEncoder;


    /*
    memberEntity 와 partnerEntity 를 최초 생성함.
    단, memberEntity 는 관련기능은 후속추가되어 클래스 추가관리되었고 @Transactional 관리를 위해
     controller 에서 서비스 호출하지 않고 partnerService 내에서 관련로직 호출함

     */

    @PostMapping("/partners")
    public ResponseEntity<CreatePartner.Response> createPartner(
            @RequestBody @Valid CreatePartner.Request request) {
        log.info("Creating partner : {}", request);

        PartnerDto partnerDto = partnerService.createPartner
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
        단, memberEntity 는 관련기능은 후속추가되어  클래스 추가관리되었고 @Transactional 관리를 위해
        controller 에서 서비스 호출하지 않고 userService 내에서 관련로직 호출함
     */

    @PostMapping("/users")
    public ResponseEntity<CreateUser.Response> createUser(
            @RequestBody @Valid CreateUser.Request request) {

        log.info("Creating user request received : {}", request.getUserId());

        UserDto userDto = userService.createUser(
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
    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody @Validated SignAuth.SignIn signIn) {

        UserDetails userDetails = memberAuthService.authenticate(signIn.getId(), signIn.getPassword());

        String token = jwtHandler.generateToken(userDetails.getUsername(), userDetails.getAuthorities());

        return ResponseEntity.ok(token);
    }


}
