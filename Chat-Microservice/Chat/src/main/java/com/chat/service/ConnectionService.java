package com.chat.service;

import com.chat.dtos.ConnectionDTO;
import com.chat.dtos.builders.ConnectionBuilder;
import com.chat.dtos.user.UserDTO;
import com.chat.entity.*;
import com.chat.errors.exceptions.UserNotFoundException;
import com.chat.repository.ConnectionRepository;
import com.chat.repository.UserConnectionRepository;
import com.chat.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ConnectionService {

    private final ConnectionRepository connectionRepository;

    private final UserRepository userRepository;

    private final UserConnectionRepository userConnectionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ConnectionService(ConnectionRepository connectionRepository, UserRepository userRepository, UserConnectionRepository userConnectionRepository, SimpMessagingTemplate messagingTemplate) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
        this.userConnectionRepository = userConnectionRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public ResponseEntity<?> takeConnection(String userId, String connectionId) throws UserNotFoundException {
        Optional<User> user = this.userRepository.findById(UUID.fromString(userId));
        if (user.isPresent()) {
            Optional<Connection> connection = this.connectionRepository.findById(UUID.fromString(connectionId));
            if (connection.isPresent()) {
                connection.get().setAdminUser(user.get());
                this.connectionRepository.save(connection.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>("Connection not found", HttpStatus.BAD_REQUEST);
        }
        throw new UserNotFoundException("Usser not found", null);
    }

    @Transactional
    public ResponseEntity<?> getConnectionsForUser(String usrId) throws UserNotFoundException {
        Optional<User> user = this.userRepository.findById(UUID.fromString(usrId));

        if (user.isPresent()) {
            if (user.get().getRole().equals(Role.ADMIN)) {
                List<Connection> connections = this.connectionRepository.findAllByAdminUserOrAdminUserNull(user.get());
                return new ResponseEntity<>(connections.stream().peek(c -> c.getMessages().sort(Comparator.comparing(Message::getSendDate))).map(ConnectionBuilder::convertEntityToDTO).toList(), HttpStatus.OK);
            }
            return new ResponseEntity<>(this.userConnectionRepository.findAllByUser(user.get()).stream().map(UserConnection::getConnection).peek(c -> c.getMessages().sort(Comparator.comparing(Message::getSendDate))).map(ConnectionBuilder::convertEntityToDTO).toList(), HttpStatus.OK);
        }
        throw new UserNotFoundException("user not found", null);
    }

    public ResponseEntity<?> createGroupConnection(ConnectionDTO connectionDTO) {
        Optional<User> adminUser = this.userRepository.findByUsername(connectionDTO.getAdminUser().getUsername());

        if (adminUser.isPresent()) {

            var connection = Connection.builder()
                    .usersConnections(new ArrayList<>())
                    .adminUser(adminUser.get())
                    .build();
            this.connectionRepository.save(connection);
            for (UserDTO user :
                    connectionDTO.getConnectedUsers()) {
                Optional<User> optionalUser = this.userRepository.findByUsername(user.getUsername());
                optionalUser.ifPresent( u ->{
                    connection.getUsersConnections().add(this.userConnectionRepository.save(UserConnection.builder()
                                    .user(u)
                                    .connection(connection)
                            .build()));
                });
            }
            this.awareClients(connection);
            return new ResponseEntity<>(ConnectionBuilder.convertEntityToDTO(connection), HttpStatus.OK);
        }
        throw new UserNotFoundException("User not found", null);
    }

    private void awareClients(Connection connection){
        ConnectionDTO dto = ConnectionBuilder.convertEntityToDTO(connection);
        for (UserDTO userDTO : dto.getConnectedUsers()){
            this.messagingTemplate.convertAndSend("/topic/connection/user/" + userDTO.getId(), dto);
        }
    }

}
