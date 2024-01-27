package com.UserManagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData{
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String role;
    private String operation;
}