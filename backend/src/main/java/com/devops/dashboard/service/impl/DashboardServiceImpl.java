package com.devops.dashboard.service.impl;

import com.devops.dashboard.dto.common.DashboardStatsResponse;
import com.devops.dashboard.entity.TaskStatus;
import com.devops.dashboard.repository.ProjectRepository;
import com.devops.dashboard.repository.TaskRepository;
import com.devops.dashboard.service.ActivityLogService;
import com.devops.dashboard.service.DashboardService;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ActivityLogService activityLogService;

    @Override
    public DashboardStatsResponse getDashboardStats() {
        var progress = projectRepository.findAll().stream()
                .map(project -> new DashboardStatsResponse.ProgressPoint(
                        project.getName(),
                        taskRepository.count((root, query, cb) -> cb.equal(root.get("project").get("id"), project.getId())),
                        taskRepository.count((root, query, cb) -> cb.and(
                                cb.equal(root.get("project").get("id"), project.getId()),
                                cb.equal(root.get("status"), TaskStatus.COMPLETED)
                        ))
                ))
                .toList();

        var activities = activityLogService.getRecentActivities().stream()
                .map(log -> new DashboardStatsResponse.ActivityItem(
                        log.getId(),
                        log.getActorName(),
                        log.getAction(),
                        log.getTargetType(),
                        log.getTargetName(),
                        log.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                ))
                .toList();

        return new DashboardStatsResponse(
                projectRepository.count(),
                taskRepository.count(),
                taskRepository.countByStatus(TaskStatus.COMPLETED),
                taskRepository.count() - taskRepository.countByStatus(TaskStatus.COMPLETED),
                progress,
                activities
        );
    }
}
