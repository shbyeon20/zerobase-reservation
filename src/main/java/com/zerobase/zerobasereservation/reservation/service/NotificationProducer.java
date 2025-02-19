package com.zerobase.zerobasereservation.reservation.service;

import static com.zerobase.zerobasereservation.rabbitmq.RabbitmqConfiguration.EXCHANGE_RESERVATION;
import static com.zerobase.zerobasereservation.rabbitmq.RabbitmqConfiguration.ROUTING_KEY_REJECT;

import com.zerobase.zerobasereservation.reservation.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendReservationRejectNotification(String memberId, String reservationId) {

        log.info("send notification message: start");

        NotificationMessage notificationMessage = this.createRejectNotification(memberId,reservationId);
        rabbitTemplate.convertAndSend(EXCHANGE_RESERVATION,ROUTING_KEY_REJECT,notificationMessage);

        log.info("send notification message: {}", notificationMessage);
    }

    private NotificationMessage createRejectNotification(String memberId, String reservationId) {
        return NotificationMessage.builder().memberId(memberId).message("reservationId : "+reservationId+" 예약 거절되었습니다").build();
    }


}
