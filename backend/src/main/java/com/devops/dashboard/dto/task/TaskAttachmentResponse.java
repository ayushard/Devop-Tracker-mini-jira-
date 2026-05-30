package com.devops.dashboard.dto.task;

import java.time.LocalDateTime;

public record TaskAttachmentResponse(
        Long id,
        String fileName,
        String fileUrl,
        LocalDateTime uploadedAt
) {
}
