package com.zerobase.zerobasereservation.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/websocket")
@RequiredArgsConstructor
public class WebSocketTestController {

    private final NotificationWebSocketHandler notificationWebSocketHandler;

    @PostMapping(value = "/test/{userID}")
    public ResponseEntity<String> sendMessage(@PathVariable String userID) {
        try {
            notificationWebSocketHandler.sendNotification(userID, "테스트 메세지");
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}



