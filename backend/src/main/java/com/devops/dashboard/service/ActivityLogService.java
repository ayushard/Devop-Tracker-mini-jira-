package com.devops.dashboard.service;

import com.devops.dashboard.entity.ActivityLog;
import java.util.List;

public interface ActivityLogService {
    void log(String actorName, String action, String targetType, String targetName);
    List<ActivityLog> getRecentActivities();
}
