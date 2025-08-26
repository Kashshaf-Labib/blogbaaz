package com.classmate.AuthService.services.impl;

import com.classmate.AuthService.clients.UserServiceClient;
import com.classmate.AuthService.dtos.AuthRequest;
import com.classmate.AuthService.dtos.AuthResponse;
import com.classmate.AuthService.dtos.UserDto;
import com.classmate.AuthService.services.AuthService;
import com.classmate.AuthService.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        try {
            UserDto user = userServiceClient.getUserByEmail(request.getEmail());

            if (user != null && user.getPassword() != null &&
                    passwordEncoder.matches(request.getPassword(), user.getPassword())) {

                String token = jwtUtil.generateToken(user.getEmail(), user.getUserId());

                return AuthResponse.builder()
                        .token(token)
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .build();
            }

            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse register(AuthRequest request) {
        try {
            UserDto newUser = UserDto.builder()
                    .name(request.getEmail().split("@")[0]) // Simple name generation
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .build();

            UserDto savedUser = userServiceClient.createUser(newUser);

            String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getUserId());

            return AuthResponse.builder()
                    .token(token)
                    .userId(savedUser.getUserId())
                    .email(savedUser.getEmail())
                    .name(savedUser.getName())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public UserDto validateToken(String token) {
        try {
            String email = jwtUtil.extractUsername(token);
            if (jwtUtil.validateToken(token, email)) {
                return userServiceClient.getUserByEmail(email);
            }
            throw new RuntimeException("Invalid token");
        } catch (Exception e) {
            throw new RuntimeException("Token validation failed: " + e.getMessage());
        }
    }
}
