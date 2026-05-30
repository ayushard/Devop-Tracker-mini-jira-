package com.devops.dashboard.service.impl;

import com.devops.dashboard.entity.ActivityLog;
import com.devops.dashboard.repository.ActivityLogRepository;
import com.devops.dashboard.service.ActivityLogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Override
    public void log(String actorName, String action, String targetType, String targetName) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setActorName(actorName);
        activityLog.setAction(action);
        activityLog.setTargetType(targetType);
        activityLog.setTargetName(targetName);
        activityLogRepository.save(activityLog);
    }

    @Override
    public List<ActivityLog> getRecentActivities() {
        return activityLogRepository.findTop10ByOrderByCreatedAtDesc();
    }
}
