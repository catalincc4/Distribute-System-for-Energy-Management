package com.chat.dtos.builders;

import com.chat.dtos.ConnectionDTO;
import com.chat.entity.Connection;
import com.chat.entity.UserConnection;

public class ConnectionBuilder {
    public static ConnectionDTO convertEntityToDTO(Connection connection){
        return ConnectionDTO.builder()
                .id(connection.getId().toString())
                .adminUser(connection.getAdminUser()==null ? null : UserBuilder.convertEntityToDTO(connection.getAdminUser()))
                .messages(connection.getMessages() == null ? null : connection.getMessages().stream().map(MessageBuilder::convertEntityToDTO).toList())
                .connectedUsers(
                        connection.getUsersConnections().stream().map(UserConnection::getUser).map(UserBuilder::convertEntityToDTO).toList()
                )
                .build();
    }
}
