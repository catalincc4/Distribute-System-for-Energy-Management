package com.UserManagement.errors.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UserNotFoundException extends UserManagementException{
    private static final String MESSAGE = "User not found";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    public UserNotFoundException(String resource, List<String> errors) {
        super(MESSAGE, STATUS, resource, errors);
    }
}
