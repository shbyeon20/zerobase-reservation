package com.zerobase.zerobasereservation.rabbitmq;

import com.rabbitmq.client.AMQP.Exchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {

    public static final String QUEUE_ALARM_SOCKET = "queue.alarm.socket";
    public static final String QUEUE_ALARM_PUSH = "queue.alarm.push";
    public static final String QUEUE_ALARM_SAVE = "queue.alarm.save";
    public static final String EXCHANGE_RESERVATION = "exchange.reservation";
    public static final String ROUTING_KEY_REJECT = "reject";

    @Bean
    public Queue queueAlarmSocket() {
        return new Queue(QUEUE_ALARM_SOCKET,true);
    }

    @Bean
    public Queue queueAlarmPush() {
        return new Queue(QUEUE_ALARM_PUSH,true);
    }

    @Bean
    public Queue queueAlarmSave() {
        return new Queue(QUEUE_ALARM_SAVE,true);
    }


    @Bean
    public DirectExchange exchangeAlarm() {
        return new DirectExchange(EXCHANGE_RESERVATION);
    }

    @Bean
    public Binding bindAlarmSocket(Queue queueAlarmSocket, DirectExchange directExchange) {
        return BindingBuilder.bind(queueAlarmSocket).to(directExchange).with(ROUTING_KEY_REJECT);
    }

    @Bean
    public Binding bindAlarmPush(Queue queueAlarmPush, DirectExchange directExchange) {
        return BindingBuilder.bind(queueAlarmPush).to(directExchange).with(ROUTING_KEY_REJECT);
    }

    @Bean
    public Binding bindAlarmSAVE(Queue queueAlarmSave, DirectExchange directExchange) {
        return BindingBuilder.bind(queueAlarmSave).to(directExchange).with(ROUTING_KEY_REJECT);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter); // 🔥 JSON 변환 설정
        return rabbitTemplate;
    }




}
