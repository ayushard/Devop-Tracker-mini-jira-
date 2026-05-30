package com.devops.dashboard.controller;

import com.devops.dashboard.dto.common.ApiResponse;
import com.devops.dashboard.service.ActivityLogService;
import com.devops.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ActivityLogService activityLogService;

    @GetMapping("/stats")
    public ApiResponse<?> getStats() {
        return new ApiResponse<>(true, "Dashboard stats fetched successfully", dashboardService.getDashboardStats());
    }

    @GetMapping("/activities")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getActivities() {
        return new ApiResponse<>(true, "Activities fetched successfully", activityLogService.getRecentActivities());
    }
}
