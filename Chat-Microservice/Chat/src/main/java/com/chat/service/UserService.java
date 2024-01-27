package com.chat.service;

import com.chat.dtos.user.UserData;
import com.chat.entity.Role;
import com.chat.entity.User;
import com.chat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(UserData userDTO) {
        var user =
                User.builder()
                        .id(UUID.fromString(userDTO.getId()))
                        .username(userDTO.getUsername())
                        .role(Role.valueOf(userDTO.getRole()))
                        .lastname(userDTO.getLastname())
                        .firstname(userDTO.getFirstname())
                        .build();
        this.userRepository.save(user);
    }

    public void deleteUser(UserData userDTO){
        this.userRepository.deleteById(UUID.fromString(userDTO.getId()));
    }

    public void updateUser(UserData userDTO){
        Optional<User> user = this.userRepository.findById(UUID.fromString(userDTO.getId()));
        if(user.isPresent()){
            user.get().setUsername(userDTO.getUsername());
            user.get().setRole(Role.valueOf(userDTO.getRole()));
            user.get().setFirstname(userDTO.getFirstname());
            user.get().setLastname(userDTO.getLastname());
            this.userRepository.save(user.get());
        }
    }
}
