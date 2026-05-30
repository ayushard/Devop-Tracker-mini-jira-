package com.devops.dashboard.dto.task;

import java.time.LocalDateTime;

public record TaskCommentResponse(
        Long id,
        String content,
        String authorName,
        LocalDateTime createdAt
) {
}
