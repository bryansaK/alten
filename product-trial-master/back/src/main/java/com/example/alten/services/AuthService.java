package com.example.alten.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.alten.dto.UserDTO;
import com.example.alten.entity.User;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder encoder;
    @Autowired
    public AuthService(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    public User loginUser(UserDTO userDTO) throws Exception {
        User user = userService.findByEmail(userDTO.getEmail());
        if (encoder.matches(userDTO.getPassword(), user.getPassword())) {
            return user;
        } else {
            throw new Exception("Invalid credentials");
        }
        //ne pas oublier d'envoyer le jwt token dans le controller
    }
    public User registerUser(UserDTO userDTO) throws Exception {
        User newUser = new User(userDTO.getUsername(), userDTO.getEmail(), encoder.encode(userDTO.getPassword()));
        return userService.saveUser(newUser);
    }
}
