package com.devops.dashboard.dto.user;

public record UserSummary(
        Long id,
        String fullName,
        String email
) {
}
