package com.devops.dashboard.dto.user;

import com.devops.dashboard.entity.Role;
import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        Set<Role> roles,
        LocalDateTime createdAt
) {
}
