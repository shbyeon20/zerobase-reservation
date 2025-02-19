package com.zerobase.zerobasereservation.websocket;

import static com.zerobase.zerobasereservation.rabbitmq.RabbitmqConfiguration.QUEUE_ALARM_SOCKET;

import com.zerobase.zerobasereservation.reservation.dto.NotificationMessage;
import javax.management.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = QUEUE_ALARM_SOCKET)
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationWebSocketHandler notificationWebSocketHandler;

    @RabbitHandler
    public void process(NotificationMessage notificationMessage)
        throws Exception {
        log.info("Received notification message: {} memberId : {}", notificationMessage.getMessage(), notificationMessage.getMemberId());
        notificationWebSocketHandler.sendNotification(notificationMessage.getMemberId(),notificationMessage.getMessage());

    }


}
