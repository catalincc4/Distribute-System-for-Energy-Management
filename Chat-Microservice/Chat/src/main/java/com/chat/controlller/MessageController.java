package com.chat.controlller;

import com.chat.dtos.MessageDTO;
import com.chat.errors.exceptions.UserNotFoundException;
import com.chat.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/add-connection")
    public ResponseEntity<?> addConnection(@RequestBody MessageDTO messageDTO) throws UserNotFoundException {
        return this.messageService.addConnection(messageDTO);
    }

    @PutMapping("/add-message")
    public ResponseEntity<?> addMessage(@RequestBody MessageDTO messageDTO) throws UserNotFoundException {
        messagingTemplate.convertAndSend("/topic/connection/" + messageDTO.getConnectionId().toLowerCase(), messageDTO);
        return this.messageService.addMessage(messageDTO);
    }

    @GetMapping("/get-messages/{connectionId}")
    public ResponseEntity<?> getMessagesByConnection(@PathVariable("connectionId") String connectionId){
        return messageService.getMessagesByConnection(connectionId);
    }

    @PutMapping("/message-seen")
    public ResponseEntity<?> setAsSeenMessage(@RequestBody String[] messageId){
        return this.messageService.setAsSeenMessage(messageId);
    }

}
