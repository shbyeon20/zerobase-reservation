package com.zerobase.zerobasereservation.controller;

import com.zerobase.zerobasereservation.dto.*;
import com.zerobase.zerobasereservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    /*
     최초로 user가 reservation을 생성함
     단, reservation ID는 UUID로 자동생성되어 유저에게 전달되고 예약변경시 확인됨
     */

    @PostMapping
    public ResponseEntity<CreateReservation.Response> createReservation(
            @RequestBody @Valid CreateReservation.Request request) {

        ReservationDto reservationDto = reservationService.createReservation(
                request.getUserId(),
                request.getStoreId(),
                request.getReservationTime());

        return ResponseEntity.ok(CreateReservation.Response.fromDto(reservationDto));
    }


    /*
        생성된 Reservation을 매장점주가 storeId를 통해서 조회를함

     */
    @GetMapping()
    public ResponseEntity<List<GetReservationsByPartner.Response>> getReservationsByPartner(
        @RequestParam String partnerId, @RequestParam String storeId){



        log.info("Get controller start for fetching reservation " +
                " by store "+storeId);

        List<ReservationDto> reservationDtos =
                reservationService.getReservationsByPartner(partnerId, storeId);

        return ResponseEntity.ok(
                reservationDtos.stream().map(GetReservationsByPartner.Response::fromDto).toList());

    }

    /*
        생성된 Reservation을 유저가 userId와 storeId를 통해서 조회를 함

     */

    @GetMapping()
    public ResponseEntity<List<GetReservationsByUser.Response>> getReservationsByUserId(
        @RequestParam String userId, @RequestParam String storeId
            ){
        log.info("Get controller start for fetching reservation ");

        List<ReservationDto> reservationDtos =
                reservationService.searchReservationsByUser(userId, storeId);

        return ResponseEntity.ok(
                reservationDtos.stream().map(GetReservationsByUser.Response::fromDto).toList());

    }



    /*
         매장주인이 예약 Id를 조회하여 reserved 상태에서 accepted상태로 변경함

     */


    @PatchMapping("/{reservationId}")
    public ResponseEntity<UpdateStatusReservation.Response> acceptReservation(
        @PathVariable String reservationId,
            @RequestBody @Valid  UpdateStatusReservation.Request request){



        log.info("Patch controller start for confirming reservation status " +
                "using resrvationId : "+reservationId);


        ReservationDto reservationDto =
                reservationService.acceptReservation(request.getMemberId(), reservationId,request.getStatus());

        return ResponseEntity.ok(UpdateStatusReservation.Response.fromDto(reservationDto));

    }



}
