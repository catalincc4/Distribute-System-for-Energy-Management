package com.UserManagement.dtos.builders;

import com.UserManagement.dtos.UserDTO;
import com.UserManagement.entity.User;

public class UserBuilder {
    public static UserDTO convertEntityToDTO(User user){
        return UserDTO
                .builder()
                .id(user.getId().toString())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .role(user.getRole().toString())
                .build();
    }
}
