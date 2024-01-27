package com.chat.service;

import com.chat.dtos.MessageDTO;
import com.chat.dtos.builders.MessageBuilder;
import com.chat.entity.Connection;
import com.chat.entity.Message;
import com.chat.entity.User;
import com.chat.entity.UserConnection;
import com.chat.errors.exceptions.UserNotFoundException;
import com.chat.repository.ConnectionRepository;
import com.chat.repository.MessageRepository;
import com.chat.repository.UserConnectionRepository;
import com.chat.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.*;

@Service
public class MessageService {

    private final ConnectionRepository connectionRepository;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;
    private final UserConnectionRepository userConnectionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageService(ConnectionRepository connectionRepository, MessageRepository messageRepository, UserRepository userRepository, UserConnectionRepository userConnectionRepository, SimpMessagingTemplate messagingTemplate) {
        this.connectionRepository = connectionRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.userConnectionRepository = userConnectionRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public ResponseEntity<?> addConnection(MessageDTO messageDTO) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(UUID.fromString(messageDTO.getSender().getId()));

        if (user.isPresent()) {
            var connection =
                    Connection.builder()
                            .adminUser(null)
                            .messages(null)
                            .usersConnections(null)
                            .build();
            this.connectionRepository.save(connection);

            var userConnection =
                    UserConnection.builder()
                            .user(user.get())
                            .connection(connection)
                            .build();
            this.userConnectionRepository.save(userConnection);

            var message =
                    Message.builder()
                            .connection(connection)
                            .sender(user.get())
                            .status("SEND")
                            .sendDate(new Date())
                            .message(messageDTO.getMessage())
                            .build();
            this.messageRepository.save(message);
            messagingTemplate.convertAndSend("/topic/messages", MessageBuilder.convertEntityToDTO(message));
            return new ResponseEntity<>(MessageBuilder.convertEntityToDTO(message), HttpStatus.OK);
        }
        throw new UserNotFoundException("User not found", null);
    }

    public ResponseEntity<?> addMessage(MessageDTO messageDTO) {
        Optional<Connection> connection = this.connectionRepository.findById(UUID.fromString(messageDTO.getConnectionId()));
        if (connection.isPresent()) {
            Optional<User> user = userRepository.findById(UUID.fromString(messageDTO.getSender().getId()));
            var message =
                    Message.builder()
                            .sender(user.get())
                            .message(messageDTO.getMessage())
                            .status("SEND")
                            .sendDate(new Date())
                            .connection(connection.get())
                            .build();
            this.messageRepository.save(message);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Connection dosen't exist", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<?> getMessagesByConnection(String connectionId) {
        Optional<Connection> connection = this.connectionRepository.findById(UUID.fromString(connectionId));
        if (connection.isPresent()) {
            return new ResponseEntity<>(connection.get().getMessages().stream().map(MessageBuilder::convertEntityToDTO).toList(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Connection not found", HttpStatus.BAD_REQUEST);
    }

    @Transactional

    public ResponseEntity<?> setAsSeenMessage(String[] messageId) {
        List<Message> messageList = new ArrayList<>();
        for (String id : messageId) {
            Optional<Message> message = this.messageRepository.findById(UUID.fromString(id));
            if (message.isPresent()) {
                message.get().setStatus("SEEN");
                this.messageRepository.save(message.get());
                messagingTemplate.convertAndSend("/topic/connection/" + message.get().getConnection().getId().toString().toLowerCase(), MessageBuilder.convertEntityToDTO(message.get()));
                messageList.add(message.get());
            }
        }
        return new ResponseEntity<>(messageList.stream().map(MessageBuilder::convertEntityToDTO).toList(), HttpStatus.OK);
    }
}
