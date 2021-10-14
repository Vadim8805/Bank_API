package com.example.bankapi.service;

import com.example.bankapi.exceptions.ResourceNotFoundException;
import com.example.bankapi.model.User;
import com.example.bankapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(int id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException("Держателя карты с таким id не существует.");
        }
        return user;
    }
}
