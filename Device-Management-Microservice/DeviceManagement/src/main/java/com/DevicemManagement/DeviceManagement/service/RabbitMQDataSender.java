package com.DevicemManagement.DeviceManagement.service;

import com.DevicemManagement.DeviceManagement.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQDataSender {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routingKey}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQDataSender.class);
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQDataSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Message message){
        LOGGER.info(String.format("Message sent -> %s", message.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
