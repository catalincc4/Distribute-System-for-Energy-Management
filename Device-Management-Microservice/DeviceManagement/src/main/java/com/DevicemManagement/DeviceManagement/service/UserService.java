package com.DevicemManagement.DeviceManagement.service;

import com.DevicemManagement.DeviceManagement.entity.User;
import com.DevicemManagement.DeviceManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> addUser(UUID user){
        this.userRepository.save(User
                .builder()
                .id(user)
                .build()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public ResponseEntity<?> deleteUser(UUID user){
        this.userRepository.deleteById(user);
        return  new ResponseEntity<>(HttpStatus.OK);
    }


}
