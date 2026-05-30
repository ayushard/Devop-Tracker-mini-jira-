package com.devops.dashboard.controller;

import com.devops.dashboard.dto.common.ApiResponse;
import com.devops.dashboard.dto.task.CommentRequest;
import com.devops.dashboard.dto.task.TaskRequest;
import com.devops.dashboard.dto.task.TaskStatusUpdateRequest;
import com.devops.dashboard.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ApiResponse<?> getTasks(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long assignedUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        return new ApiResponse<>(true, "Tasks fetched successfully",
                taskService.getTasks(search, status, projectId, assignedUserId, page, size, authentication.getName()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> createTask(@Valid @RequestBody TaskRequest request, Authentication authentication) {
        return new ApiResponse<>(true, "Task created successfully", taskService.createTask(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request, Authentication authentication) {
        return new ApiResponse<>(true, "Task updated successfully", taskService.updateTask(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> deleteTask(@PathVariable Long id, Authentication authentication) {
        taskService.deleteTask(id, authentication.getName());
        return new ApiResponse<>(true, "Task deleted successfully", null);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<?> updateStatus(@PathVariable Long id, @Valid @RequestBody TaskStatusUpdateRequest request, Authentication authentication) {
        return new ApiResponse<>(true, "Task status updated successfully", taskService.updateStatus(id, request, authentication.getName()));
    }

    @PostMapping("/{id}/comments")
    public ApiResponse<?> addComment(@PathVariable Long id, @Valid @RequestBody CommentRequest request, Authentication authentication) {
        return new ApiResponse<>(true, "Comment added successfully", taskService.addComment(id, request, authentication.getName()));
    }

    @PostMapping("/{id}/attachments")
    public ApiResponse<?> uploadAttachment(@PathVariable Long id, @RequestPart("file") MultipartFile file, Authentication authentication) {
        return new ApiResponse<>(true, "Attachment uploaded successfully", taskService.uploadAttachment(id, file, authentication.getName()));
    }
}
