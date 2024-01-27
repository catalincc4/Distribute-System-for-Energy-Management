package com.UserManagement.service;

import com.UserManagement.dtos.UserData;
import com.UserManagement.entity.Role;
import com.UserManagement.entity.User;
import com.UserManagement.repository.UserRepository;
import com.UserManagement.security.auth.AuthenticationRequest;
import com.UserManagement.security.auth.AuthenticationResponse;
import com.UserManagement.security.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final RabbitMQDataSender sender;

    public ResponseEntity<AuthenticationResponse> register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT)
                .build();
        userRepository.save(user);
        sender.sendMessage(UserData.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole().toString())
                .operation("ADD")
                .build());
        var token = jwtService.generateToken(user);
        return new ResponseEntity<>(AuthenticationResponse.builder().token(token).build(), HttpStatus.OK);
    }

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername());
        if (user.isPresent()) {
            var token = jwtService.generateToken(user.get());
            return new ResponseEntity<>(AuthenticationResponse.builder().token(token).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
