package com.classmate.UserService.services;

import com.classmate.UserService.entities.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(String userId);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(String userId, User user);
    void deleteUser(String userId);
    User authenticateUser(String email, String password);
}
