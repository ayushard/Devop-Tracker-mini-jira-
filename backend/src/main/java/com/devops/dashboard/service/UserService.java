package com.devops.dashboard.service;

import com.devops.dashboard.dto.user.UserResponse;
import com.devops.dashboard.dto.user.UserSummary;
import com.devops.dashboard.entity.User;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    List<UserSummary> getAllDevelopers();
    User findByEmail(String email);
}
