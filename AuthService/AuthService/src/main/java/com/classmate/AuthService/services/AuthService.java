package com.classmate.AuthService.services;

import com.classmate.AuthService.dtos.AuthRequest;
import com.classmate.AuthService.dtos.AuthResponse;
import com.classmate.AuthService.dtos.UserDto;

public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
    AuthResponse register(AuthRequest request);
    UserDto validateToken(String token);
}
