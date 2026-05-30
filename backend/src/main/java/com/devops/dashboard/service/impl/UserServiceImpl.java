package com.devops.dashboard.service.impl;

import com.devops.dashboard.dto.user.UserResponse;
import com.devops.dashboard.dto.user.UserSummary;
import com.devops.dashboard.entity.Role;
import com.devops.dashboard.entity.User;
import com.devops.dashboard.exception.ResourceNotFoundException;
import com.devops.dashboard.repository.UserRepository;
import com.devops.dashboard.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRoles(), user.getCreatedAt()))
                .toList();
    }

    @Override
    public List<UserSummary> getAllDevelopers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(Role.ROLE_DEVELOPER))
                .map(user -> new UserSummary(user.getId(), user.getFullName(), user.getEmail()))
                .toList();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
