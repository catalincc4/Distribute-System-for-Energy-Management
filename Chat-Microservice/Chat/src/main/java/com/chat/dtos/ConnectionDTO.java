package com.chat.dtos;

import com.chat.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDTO {
    private String id;
    private UserDTO adminUser;
    private List<UserDTO> connectedUsers;
    private List<MessageDTO> messages;
}
