package com.devops.dashboard.service;

import com.devops.dashboard.dto.auth.AuthResponse;
import com.devops.dashboard.dto.auth.LoginRequest;
import com.devops.dashboard.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
