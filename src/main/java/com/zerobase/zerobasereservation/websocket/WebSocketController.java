package com.zerobase.zerobasereservation.websocket;

import static org.springframework.http.ResponseEntity.internalServerError;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/websocket")
@RequiredArgsConstructor
public class WebSocketController {

    private final NotificationWebSocketHandler notificationWebSocketHandler;

    @PostMapping(value = "/test/{userID}")
    public ResponseEntity<String> sendMessage(@PathVariable String userID)
         {
             try {
                 notificationWebSocketHandler.sendNotification(userID,"테스트 메세지");
                 return ResponseEntity.ok().build();

             } catch (Exception e) {
                 return ResponseEntity.internalServerError().build();
             }
    }

}


