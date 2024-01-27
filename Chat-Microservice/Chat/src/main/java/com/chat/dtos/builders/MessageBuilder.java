package com.chat.dtos.builders;


import com.chat.dtos.MessageDTO;
import com.chat.entity.Message;

public class MessageBuilder {
    public static MessageDTO convertEntityToDTO(Message message){
        return MessageDTO.builder()
                .id(message.getId().toString())
                .connectionId(message.getConnection().getId().toString())
                .message(message.getMessage())
                .sender(UserBuilder.convertEntityToDTO(message.getSender()))
                .sendDate(message.getSendDate())
                .status(message.getStatus())
                .build();
    }
}
