package com.example.alten.services;

import java.time.LocalDateTime;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.alten.dto.LoginRequest;
import com.example.alten.dto.RegisterRequest;
import com.example.alten.entity.User;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder encoder;

    public AuthService(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    public User loginUser(LoginRequest request) throws Exception {
        User user = userService.findByEmail(request.getEmail());

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return user;
    }

    public User registerUser(RegisterRequest request) {
        User user = new User(
            request.getUsername(),
            request.getEmail(),
            encoder.encode(request.getPassword()),
            request.getFirstname()
        );

        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        return userService.saveUser(user);
    }
}