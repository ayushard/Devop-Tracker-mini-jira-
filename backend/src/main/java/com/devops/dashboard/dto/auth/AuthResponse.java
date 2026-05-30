package com.devops.dashboard.dto.auth;

import com.devops.dashboard.dto.user.UserResponse;

public record AuthResponse(
        String token,
        UserResponse user
) {
}
