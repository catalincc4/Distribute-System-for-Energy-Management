package com.UserManagement.service;

import com.UserManagement.dtos.UserDTO;
import com.UserManagement.dtos.UserData;
import com.UserManagement.dtos.builders.UserBuilder;
import com.UserManagement.entity.Role;
import com.UserManagement.entity.User;
import com.UserManagement.errors.exceptions.UserInsertException;
import com.UserManagement.errors.exceptions.UserNotFoundException;
import com.UserManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${microservice.name}")
    private String microserviceName;

    private final RabbitMQDataSender sender;

    public UserService(RabbitMQDataSender sender) {
        this.sender = sender;
    }

    public ResponseEntity<?> addUser(UserDTO user) throws UserInsertException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User user1 = User.builder()
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(Role.valueOf(user.getRole()))
                .password(user.getPassword())
                .build();
        this.userRepository.saveAndFlush(user1);
        restTemplate.postForEntity("http://" + microserviceName + ":8080/api/user/addUser", user1.getId(), HttpStatus.class);
        sender.sendMessage(UserData.builder()
                .id(user1.getId().toString())
                .username(user1.getUsername())
                .firstname(user1.getFirstname())
                .lastname(user1.getLastname())
                .role(user1.getRole().toString())
                .operation("ADD")
                .build());
        return new ResponseEntity<>(UserBuilder.convertEntityToDTO(user1), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUser(String username) throws UserNotFoundException {
        Optional<User> dbUser = this.userRepository.findByUsername(username);
        if (dbUser.isPresent()) {
            userRepository.delete(dbUser.get());
            restTemplate.delete("http://" + microserviceName + ":8080/api/user/deleteUser/{id}", dbUser.get().getId());
            sender.sendMessage(UserData.builder()
                    .id(dbUser.get().getId().toString())
                    .operation("DELETE")
                    .build());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new UserNotFoundException("User not found", null);
    }

    public ResponseEntity<?> updateUser(UserDTO user) {
        Optional<User> dbUser = this.userRepository.findById(UUID.fromString(user.getId()));
        if (dbUser.isPresent()) {
            User user1 = dbUser.get();
            user1.setUsername(user.getUsername());
            user1.setFirstname(user.getFirstname());
            user1.setLastname(user.getLastname());
            user1.setRole(Role.valueOf(user.getRole()));
            this.userRepository.save(user1);
            sender.sendMessage(UserData.builder()
                    .id(user1.getId().toString())
                    .username(user1.getUsername())
                    .firstname(user1.getFirstname())
                    .lastname(user1.getLastname())
                    .role(user1.getRole().toString())
                    .operation("UPDATE")
                    .build());
            return new ResponseEntity<>(UserBuilder.convertEntityToDTO(user1), HttpStatus.OK);
        }
        throw new UserNotFoundException("User not present", null);
    }

    public ResponseEntity<?> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        return new ResponseEntity<>(users.stream().map(UserBuilder::convertEntityToDTO).toList(), HttpStatus.OK);
    }
}
