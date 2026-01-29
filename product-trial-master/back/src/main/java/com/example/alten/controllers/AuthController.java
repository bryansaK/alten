package com.example.alten.controllers;

import com.ey.springboot3security.entity.AuthRequest;
import com.ey.springboot3security.entity.UserInfo;
import com.ey.springboot3security.service.JwtService;
import com.ey.springboot3security.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.annotation.*;

import com.example.alten.dto.AuthResponseDTO;
import com.example.alten.dto.UserDTO;
import com.example.alten.services.AuthService;
import com.example.alten.services.JwtService;

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
    public UserDTO createAccount(@RequestBody  userInfo) {
        return authService.addUser(userInfo);
    }

    @PostMapping("/token")
    public AuthResponseDTO authenticateAndGetToken(@RequestBody AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
            )
        );

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }
}
