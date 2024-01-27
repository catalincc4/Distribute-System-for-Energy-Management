package com.DevicemManagement.DeviceManagement.controller;

import com.DevicemManagement.DeviceManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/addUser")
    public ResponseEntity<?> addUser(@RequestBody UUID user){
        return this.userService.addUser(user);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id){
        return this.userService.deleteUser(id);
    }
}
