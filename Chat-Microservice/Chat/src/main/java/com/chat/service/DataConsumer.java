package com.chat.service;

import com.chat.dtos.user.UserData;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DataConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataConsumer.class);

    private final UserService  userService;

    public DataConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = {"${rabbitmq.queue-data.name}"})
    public void consumeData(UserData message) {
        LOGGER.info("New data received\n" + message.toString());
        if("ADD".equals(message.getOperation())){
            userService.addUser(message);
        }else if("DELETE".equals(message.getOperation())){
            userService.deleteUser(message);
        }else{
            userService.updateUser(message);
        }
    }

}
