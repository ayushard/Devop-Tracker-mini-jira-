package com.devops.dashboard.dto.task;

import com.devops.dashboard.entity.TaskPriority;
import com.devops.dashboard.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TaskRequest(
        @NotBlank String title,
        String description,
        @NotNull TaskPriority priority,
        @NotNull TaskStatus status,
        LocalDate deadline,
        @NotNull Long projectId,
        Long assignedUserId
) {
}
