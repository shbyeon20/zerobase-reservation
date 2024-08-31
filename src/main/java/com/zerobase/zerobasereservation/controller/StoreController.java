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

public class StoreController {
    private final StoreService storeService;

    //todo : validation에 관련 Exception handling 처리할것

    @PostMapping("/stores")
    public ResponseEntity<CreateStore.Response> createPartner(
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

    @GetMapping("/stores/partnerId/{partnerId}")
    public ResponseEntity<List<StoreDto>> findByPartnerId(
            @PathVariable String partnerId)
    {
        log.info("Get Controller start for store Info using partnerId :" +partnerId);
        return ResponseEntity.ok(storeService.findByPartnerId(partnerId));
    }

    @GetMapping("/stores/storeId/{storeId}")
    public ResponseEntity<StoreDto> findByStoreId(
            @PathVariable String storeId)
    {
        log.info("Get Controller start for store Info using storeId :" +storeId);
        return ResponseEntity.ok(storeService.findByStoreId(storeId));
    }





}
