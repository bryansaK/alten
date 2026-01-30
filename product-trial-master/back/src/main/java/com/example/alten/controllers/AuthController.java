package com.example.alten.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.alten.dto.AuthResponseDTO;
import com.example.alten.dto.LoginRequest;
import com.example.alten.dto.RegisterRequest;
import com.example.alten.entity.User;
import com.example.alten.services.AuthService;
import com.example.alten.services.JwtService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/account")
    public ResponseEntity<?> createAccount(@RequestBody RegisterRequest user) {
        try {
            User createdUser = authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) 
                    .body(e.getMessage());
        }
    }

    @PostMapping("/token")
    public  ResponseEntity<?> authenticateAndGetToken(@RequestBody LoginRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            String token =  jwtService.generateToken(authRequest.getEmail());
            User user = (User) authentication.getPrincipal();
            AuthResponseDTO response = new AuthResponseDTO(token, user.getUsername(), user.getEmail(), user.getFirstname(), user.getRole());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else{
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }
}
