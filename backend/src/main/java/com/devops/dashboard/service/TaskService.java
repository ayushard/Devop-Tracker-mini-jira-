package com.devops.dashboard.service;

import com.devops.dashboard.dto.common.PagedResponse;
import com.devops.dashboard.dto.task.CommentRequest;
import com.devops.dashboard.dto.task.TaskCommentResponse;
import com.devops.dashboard.dto.task.TaskRequest;
import com.devops.dashboard.dto.task.TaskResponse;
import com.devops.dashboard.dto.task.TaskStatusUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface TaskService {
    PagedResponse<TaskResponse> getTasks(String search, String status, Long projectId, Long assignedUserId, int page, int size, String currentUserEmail);
    TaskResponse createTask(TaskRequest request, String actorEmail);
    TaskResponse updateTask(Long id, TaskRequest request, String actorEmail);
    void deleteTask(Long id, String actorEmail);
    TaskResponse updateStatus(Long id, TaskStatusUpdateRequest request, String actorEmail);
    TaskCommentResponse addComment(Long taskId, CommentRequest request, String actorEmail);
    TaskResponse uploadAttachment(Long taskId, MultipartFile file, String actorEmail);
}
