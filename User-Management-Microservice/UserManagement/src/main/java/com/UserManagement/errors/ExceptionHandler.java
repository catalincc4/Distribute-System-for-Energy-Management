package com.UserManagement.errors;

import com.UserManagement.errors.exceptions.UserInsertException;
import com.UserManagement.errors.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e, WebRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(e, status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {UserInsertException.class})
    public ResponseEntity<?> handleUserInsertException(UserInsertException e, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(e, status);
    }
}
