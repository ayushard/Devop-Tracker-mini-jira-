package com.devops.dashboard.dto.project;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        String keyCode,
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime createdAt,
        long totalTasks,
        long completedTasks
) {
}
