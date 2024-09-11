package com.zerobase.zerobasereservation.controller;

import com.zerobase.zerobasereservation.dto.CreateStore;
import com.zerobase.zerobasereservation.dto.StoreDto;
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


@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;


    /*
     partner로부터 store 정보를 입력받아서 store record를 생성할것
     */

    @PostMapping("/partner/create")
    public ResponseEntity<CreateStore.Response> createStore(
            @RequestBody @Valid CreateStore.Request request) {
        log.info("Post controller start  for  store creation : "+ request.getPartnerId());

        StoreDto store = storeService.createStore(
                request.getPartnerId(),
                request.getStoreId(),
                request.getAddress(),
                request.getStoreComment()
        );

        return ResponseEntity.ok(CreateStore.Response.fromDto(store));
    }

    /*
    파트너ID로 등록된 매장의 리스트를 파트너가 조회하는 기능
     */
    @GetMapping("/partner/{partnerId}/search")
    public ResponseEntity<List<StoreDto>> findByPartnerId(
            @PathVariable String partnerId)
    {
        log.info("Get Controller start for store Info using partnerId :" +partnerId);
        return ResponseEntity.ok(storeService.findByPartnerId(partnerId));
    }

    /*
    스토어ID로 등록된 매장의 정보를 사용자가 조회하는 기능
     */

    @GetMapping("/{storeId}/user/search")
    public ResponseEntity<StoreDto> findByStoreId(
            @PathVariable String storeId)
    {
        log.info("Get Controller start for store Info using storeId :" +storeId);
        return ResponseEntity.ok(storeService.findByStoreId(storeId));
    }





}
