package com.devops.dashboard.dto.task;

import com.devops.dashboard.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateRequest(@NotNull TaskStatus status) {
}
