package com.devops.dashboard.dto.task;

import com.devops.dashboard.dto.user.UserSummary;
import com.devops.dashboard.entity.TaskPriority;
import com.devops.dashboard.entity.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskPriority priority,
        TaskStatus status,
        LocalDate deadline,
        Long projectId,
        String projectName,
        UserSummary assignedUser,
        LocalDateTime createdAt,
        List<TaskCommentResponse> comments,
        List<TaskAttachmentResponse> attachments
) {
}
