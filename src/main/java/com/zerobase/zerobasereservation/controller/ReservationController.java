package com.zerobase.zerobasereservation.controller;

import com.zerobase.zerobasereservation.dto.*;
import com.zerobase.zerobasereservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    //todo : validation에 관련 Exception handling 처리할것


    /*
     최초로 user가 reservation을 생성함
     단, reservation ID는 UUID로 자동생성되어 유저에게 전달되고 예약변경시 확인됨
     */

    @PostMapping("/user/create")
    public ResponseEntity<CreateReservation.Response> createReservation(
            @RequestBody @Valid CreateReservation.Request request) {
        log.info("Post controller start  for  store creation : "+ request.getUserId());

        ReservationDto reservationDto = reservationService.createReservation(
                request.getUserId(),
                request.getStoreId(),
                request.getReservationTime()
        );

        return ResponseEntity.ok(CreateReservation.Response.fromDto(reservationDto));
    }


    /*
        생성된 Reservation을 매장점주가 storeId를 통해서 조회를함

     */
    @PostMapping("/partner/list")
    public ResponseEntity<List<GetReservationsByPartner.Response>> getReservationsByUserId(
            @RequestBody @Valid GetReservationsByPartner.Request request){
        log.info("Get controller start for fetching reservation " +
                " by store "+request.getStoreId());

        List<ReservationDto> reservationDtos =
                reservationService.getReservationsByPartner(
                        request.getPartnerId(), request.getStoreId());

        return ResponseEntity.ok(
                reservationDtos.stream().map(GetReservationsByPartner.Response::fromDto).toList());

    }

    /*
        생성된 Reservation을 유저가 userId와 storeId를 통해서 조회를 함

     */

    @PostMapping("/user/list")
    public ResponseEntity<List<GetReservationsByUser.Response>> getReservationsByUserId(
            @RequestBody @Valid GetReservationsByUser.Request request){
        log.info("Get controller start for fetching reservation " +
                "by user : "+request.getUserId()+" by store "+request.getStoreId());


        List<ReservationDto> reservationDtos =
                reservationService.searchReservationsByUser(request.getUserId(), request.getStoreId());
        System.out.println(reservationDtos.toString());

        return ResponseEntity.ok(
                reservationDtos.stream().map(GetReservationsByUser.Response::fromDto).toList());

    }



    /*
         매장주인이 예약 Id를 조회하여 reserved 상태에서 accepted상태로 변경함

     */


    @PatchMapping("/partner/accept")
    public ResponseEntity<UpdateStatusReservation.Response> acceptReservation(
            @RequestBody @Valid  UpdateStatusReservation.Request request){
        log.info("Patch controller start for confirming reservation status " +
                "using resrvationId : "+request.getReservationId());


        ReservationDto reservationDto =
                reservationService.acceptReservation(
                        request.getReservationId()
                        );

        return ResponseEntity.ok(UpdateStatusReservation.Response.fromDto(reservationDto));

    }



    /*
              매장주인이 reserved 상태인 예약을 예약을 거절할때 rejected상태로 변경함
     */

    @PatchMapping("partner/reject")
    public ResponseEntity<UpdateStatusReservation.Response> rejectReservation(
            @RequestBody @Valid  UpdateStatusReservation.Request request){
        log.info("Patch controller start for rejecting reservation status " +
                "using resrvationId : "+ request.getReservationId());


        ReservationDto reservationDto =
                reservationService.rejectReservation(
                        request.getReservationId()
                );

        return ResponseEntity.ok(UpdateStatusReservation.Response.fromDto(reservationDto));

    }

    /*
       손님이 키오스크에 방문하여 Accepted 상태인 예약을 승인하여 confiremd상태로 변경함
       단, 10분 이전에 확정을 해야 인정됨

     */

    @PatchMapping("/kiosk/confirm")
    public ResponseEntity<UpdateStatusReservation.Response> confirmReservation(
            @RequestBody @Valid  UpdateStatusReservation.Request request){
        log.info("Patch controller start for confriming reservation status " +
                "using resrvationId : "+request.getReservationId());


        ReservationDto reservationDto =
                reservationService.confirmReservation(
                        request.getReservationId()
                );

        return ResponseEntity.ok(UpdateStatusReservation.Response.fromDto(reservationDto));

    }




}
