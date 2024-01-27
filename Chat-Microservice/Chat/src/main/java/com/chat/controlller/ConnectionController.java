package com.chat.controlller;

import com.chat.dtos.ConnectionDTO;
import com.chat.errors.exceptions.UserNotFoundException;
import com.chat.service.ConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/connection")
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PatchMapping("/take-connection/{userId}/{connectionId}")
    public ResponseEntity<?> takeConnection(@PathVariable("userId") String userId, @PathVariable("connectionId") String connectionId){
        return this.connectionService.takeConnection(userId, connectionId);
    }

    @GetMapping("/get-for-user/{userId}")
    public ResponseEntity<?> getConnectionsForUser(@PathVariable("userId") String userId) throws UserNotFoundException {
        return this.connectionService.getConnectionsForUser(userId);
    }

    @PostMapping("/create-group-connection")
    public ResponseEntity<?> createGroupConnection(@RequestBody ConnectionDTO connectionDTO){
        return this.connectionService.createGroupConnection(connectionDTO);
    }

}
