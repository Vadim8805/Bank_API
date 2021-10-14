package com.example.bankapi.repository;

import com.example.bankapi.model.User;

public interface UserRepository {
    User getUserById(int id);
}
