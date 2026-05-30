package com.devops.dashboard.dto.project;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record ProjectRequest(
        @NotBlank String name,
        String description,
        @NotBlank String keyCode,
        LocalDate startDate,
        LocalDate endDate
) {
}
