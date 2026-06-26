package com.pbo.sigaban.service;

import com.pbo.sigaban.model.User;
import com.pbo.sigaban.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // In a real application, you should hash the password using BCrypt and compare hashes
            // For PBO project simplicity, we'll use plain text comparison
            return user.getPassword().equals(password);
        }
        
        return false;
    }
}
