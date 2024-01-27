package com.UserManagement.errors.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UserInsertException extends UserManagementException{
    private final static String MESSAGE = "Cannot insert user in db";
    private final static HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public UserInsertException(String resources, List<String> errors){
        super(MESSAGE,STATUS,resources, errors);
    }
}
