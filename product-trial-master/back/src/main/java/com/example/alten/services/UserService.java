package com.example.alten.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.alten.entity.User;
import com.example.alten.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User findByEmail(String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new Exception("User not found with email: " + email);
        }
    }

    public User saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + user.getEmail());
        }
        if (user.getRole() == null) {
            user.setRole("USER");
        }
       User newUser = userRepository.save(user);
        return newUser;
    }

}
