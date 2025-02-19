package com.zerobase.zerobasereservation.alarmstore;

import static com.zerobase.zerobasereservation.rabbitmq.RabbitmqConfiguration.QUEUE_ALARM_SAVE;
import static com.zerobase.zerobasereservation.rabbitmq.RabbitmqConfiguration.QUEUE_ALARM_SOCKET;

import com.zerobase.zerobasereservation.reservation.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = QUEUE_ALARM_SAVE)
@RequiredArgsConstructor
public class NotificationSaveConsumer {

    private final AlarmLogStoreService alarmStoreService;

    @RabbitHandler
    public void process(NotificationMessage notificationMessage)
        throws Exception {
        log.info("Received notification message: {} memberId : {}", notificationMessage.getMessage(), notificationMessage.getMemberId());
        alarmStoreService.save(notificationMessage.getMemberId(),notificationMessage.getMessage());

    }


}
