package com.devops.dashboard.controller;

import com.devops.dashboard.dto.common.ApiResponse;
import com.devops.dashboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getUsers() {
        return new ApiResponse<>(true, "Users fetched successfully", userService.getAllUsers());
    }

    @GetMapping("/developers")
    public ApiResponse<?> getDevelopers() {
        return new ApiResponse<>(true, "Developers fetched successfully", userService.getAllDevelopers());
    }
}
