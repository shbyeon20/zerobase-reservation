package com.zerobase.zerobasereservation.controller;

import com.zerobase.zerobasereservation.dto.*;
import com.zerobase.zerobasereservation.service.ReservationService;
import com.zerobase.zerobasereservation.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor

public class ReservationController {
    private final StoreService storeService;
    private final ReservationService reservationService;

    //todo : validation에 관련 Exception handling 처리할것


    // 최초로 reservation을 생성함

    @PostMapping("/reservations")
    public ResponseEntity<CreateReservation.Response> createPartner(
            @RequestBody @Valid CreateReservation.Request request) {
        log.info("Post controller start  for  store creation : "+ request.getUserId());

        ReservationDto reservationDto = reservationService.createReservation(
                request.getUserId(),
                request.getStoreId(),
                request.getReservationTime()
        );

        return ResponseEntity.ok(CreateReservation.Response.fromDto(reservationDto));
    }



    // 생성된 Reservation을 매장점주가 storeId를 통해서 조회를함
    @PostMapping("/reservations/listbypartner")
    public ResponseEntity<List<GetReservationsByPartner.Response>> getReservationsByUserId(
            @RequestBody @Valid GetReservationsByPartner.Request request){
        log.info("Get controller start for fetching reservation " +
                " by store "+request.getStoreId());

        List<ReservationDto> reservationDtos =
                reservationService.getReservationsByPartner(request.getStoreId());

        return ResponseEntity.ok(
                reservationDtos.stream().map(GetReservationsByPartner.Response::fromDto).toList());

    }

    // 생성된 Reservation을 유저가 userId와 storeId를 통해서 조회를 함

    @PostMapping("/reservations/listbyuser")
    public ResponseEntity<List<GetReservationsByUser.Response>> getReservationsByUserId(
            @RequestBody @Valid GetReservationsByUser.Request request){
        log.info("Get controller start for fetching reservation " +
                "by user : "+request.getUserId()+" by store "+request.getStoreId());


        List<ReservationDto> reservationDtos =
                reservationService.getReservationsByUser(request.getUserId(), request.getStoreId());

        return ResponseEntity.ok(
                reservationDtos.stream().map(GetReservationsByUser.Response::fromDto).toList());

    }

    // 사용자가 매장에 방문하여 키오스크를 통해 예약 Id를 조회하여 reserved 상태에서 confirmed상태로 변경함

    @PatchMapping("/reservations/confirm")
    public ResponseEntity<ConfirmReservation.Response> confirmReservation(
            @RequestBody @Valid ConfirmReservation.Request request){
        log.info("Patch controller start for updating reservation " +
                "using resrvationId : "+request.getReservationId());


        ReservationDto reservationDto =
                reservationService.updateReservationsStatus(request.getReservationId());

        return ResponseEntity.ok(ConfirmReservation.Response.fromDto(reservationDto));

    }

}
