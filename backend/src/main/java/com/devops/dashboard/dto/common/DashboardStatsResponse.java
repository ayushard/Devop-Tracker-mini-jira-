package com.devops.dashboard.dto.common;

import java.util.List;

public record DashboardStatsResponse(
        long totalProjects,
        long totalTasks,
        long completedTasks,
        long pendingTasks,
        List<ProgressPoint> projectProgress,
        List<ActivityItem> recentActivities
) {
    public record ProgressPoint(String name, long totalTasks, long completedTasks) {
    }

    public record ActivityItem(Long id, String actorName, String action, String targetType, String targetName, String createdAt) {
    }
}
