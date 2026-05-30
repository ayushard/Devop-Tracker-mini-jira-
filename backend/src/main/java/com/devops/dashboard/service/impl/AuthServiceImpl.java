package com.devops.dashboard.service.impl;

import com.devops.dashboard.dto.auth.AuthResponse;
import com.devops.dashboard.dto.auth.LoginRequest;
import com.devops.dashboard.dto.auth.RegisterRequest;
import com.devops.dashboard.dto.user.UserResponse;
import com.devops.dashboard.entity.Role;
import com.devops.dashboard.entity.User;
import com.devops.dashboard.exception.BadRequestException;
import com.devops.dashboard.repository.UserRepository;
import com.devops.dashboard.security.JwtService;
import com.devops.dashboard.service.ActivityLogService;
import com.devops.dashboard.service.AuthService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ActivityLogService activityLogService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email is already registered");
        }

        Set<Role> roles = request.roles().isEmpty() ? Set.of(Role.ROLE_DEVELOPER) : request.roles();

        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        activityLogService.log(savedUser.getFullName(), "registered", "User", savedUser.getEmail());

        var userDetails = org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getEmail())
                .password(savedUser.getPassword())
                .authorities(savedUser.getRoles().stream().map(Role::name).toArray(String[]::new))
                .build();

        return new AuthResponse(
                jwtService.generateToken(userDetails),
                new UserResponse(savedUser.getId(), savedUser.getFullName(), savedUser.getEmail(), savedUser.getRoles(), savedUser.getCreatedAt())
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        var userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(Role::name).toArray(String[]::new))
                .build();

        activityLogService.log(user.getFullName(), "logged in", "User", user.getEmail());

        return new AuthResponse(
                jwtService.generateToken(userDetails),
                new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRoles(), user.getCreatedAt())
        );
    }
}
