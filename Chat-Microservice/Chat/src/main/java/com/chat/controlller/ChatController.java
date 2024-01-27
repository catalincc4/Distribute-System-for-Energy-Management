package com.chat.controlller;

import com.chat.dtos.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ChatController {

    @MessageMapping("/message/{id}")
    @SendTo("/topic/typing/{id}")
    public String processMessageFromClient(@PathVariable String id, String message) {
        return message;
    }

}
