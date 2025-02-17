package com.zerobase.zerobasereservation.reservation.controller;

import com.zerobase.zerobasereservation.reservation.dto.CreateReservation;
import com.zerobase.zerobasereservation.reservation.dto.GetReservationsByPartner;
import com.zerobase.zerobasereservation.reservation.dto.GetReservationsByUser;
import com.zerobase.zerobasereservation.reservation.dto.ReservationDto;
import com.zerobase.zerobasereservation.reservation.dto.UpdateStatusReservation;
import com.zerobase.zerobasereservation.reservation.dto.CreateReservation.Response;
import com.zerobase.zerobasereservation.reservation.service.ReservationService;
import com.zerobase.zerobasereservation.reservation.service.ReservationStatusFacade;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationStatusFacade reservationStatusFacade;

    /*
     최초로 user가 reservation을 생성함
     단, reservation ID는 UUID로 자동생성되어 유저에게 전달되고 예약변경시 확인됨
     */

    @PostMapping("/reservation")
    public ResponseEntity<Response> createReservation(
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
    @GetMapping("/partner/{partnerId}/store/{storeId}/reservation")
    public ResponseEntity<List<GetReservationsByPartner.Response>> getReservationsByPartner(
        @RequestParam String partnerId, @RequestParam String storeId){



        log.info("Get controller start for fetching reservation " +
                " by store "+storeId);

        List<ReservationDto> reservationDtos =
                reservationService.getReservationsByStore(partnerId, storeId);

        return ResponseEntity.ok(
                reservationDtos.stream().map(GetReservationsByPartner.Response::fromDto).toList());

    }

    /*
        생성된 Reservation을 유저가 userId와 storeId를 통해서 조회를 함

     */

    @GetMapping("/user/{userId}/reservation")
    public ResponseEntity<List<GetReservationsByUser.Response>> getReservationsByUserId(
        @RequestParam String userId
            ){
        log.info("Get controller start for fetching reservation ");

        List<ReservationDto> reservationDtos =
                reservationService.searchReservationsByUser(userId);

        return ResponseEntity.ok(
                reservationDtos.stream().map(GetReservationsByUser.Response::fromDto).toList());

    }



    /*
         매장주인이 예약 Id를 조회하여 reserved 상태에서 accepted상태로 변경함

     */


    @PatchMapping("reservation/{reservationId}")
    public ResponseEntity<UpdateStatusReservation.Response> updateReservationStatus(
        @PathVariable String reservationId,
            @RequestBody @Valid  UpdateStatusReservation.Request request){



        log.info("Patch controller start for confirming reservation status " +
                "using resrvationId : "+reservationId);


        ReservationDto reservationDto =
                reservationStatusFacade.readStatusAndAssignService(request.getMemberId(), reservationId,request.getStatus());

        return ResponseEntity.ok(UpdateStatusReservation.Response.fromDto(reservationDto));

    }
    
    



}
