package com.UserManagement.controller;

import com.UserManagement.dtos.UserDTO;
import com.UserManagement.entity.User;
import com.UserManagement.errors.exceptions.UserInsertException;
import com.UserManagement.errors.exceptions.UserNotFoundException;
import com.UserManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value ="/all")
    public ResponseEntity<?> getAllUsers(){
        return this.userService.getAllUsers();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserDTO user) throws UserInsertException {
        return this.userService.addUser(user);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUser/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) throws UserNotFoundException {
        return this.userService.deleteUser(username);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) throws UserNotFoundException{
        return this.userService.updateUser(userDTO);
    }
}
