package com.chat.dtos;

import com.chat.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  MessageDTO {
    private String id;
    private String message;
    private UserDTO sender;
    private String connectionId;
    private Date sendDate;
    private String status;
}
