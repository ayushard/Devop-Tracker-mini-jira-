package com.devops.dashboard.service.impl;

import com.devops.dashboard.dto.common.PagedResponse;
import com.devops.dashboard.dto.task.CommentRequest;
import com.devops.dashboard.dto.task.TaskAttachmentResponse;
import com.devops.dashboard.dto.task.TaskCommentResponse;
import com.devops.dashboard.dto.task.TaskRequest;
import com.devops.dashboard.dto.task.TaskResponse;
import com.devops.dashboard.dto.task.TaskStatusUpdateRequest;
import com.devops.dashboard.dto.user.UserSummary;
import com.devops.dashboard.entity.Project;
import com.devops.dashboard.entity.Role;
import com.devops.dashboard.entity.Task;
import com.devops.dashboard.entity.TaskAttachment;
import com.devops.dashboard.entity.TaskComment;
import com.devops.dashboard.entity.User;
import com.devops.dashboard.exception.ResourceNotFoundException;
import com.devops.dashboard.repository.ProjectRepository;
import com.devops.dashboard.repository.TaskAttachmentRepository;
import com.devops.dashboard.repository.TaskCommentRepository;
import com.devops.dashboard.repository.TaskRepository;
import com.devops.dashboard.repository.UserRepository;
import com.devops.dashboard.service.ActivityLogService;
import com.devops.dashboard.service.TaskService;
import com.devops.dashboard.service.UserService;
import jakarta.persistence.criteria.Predicate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TaskCommentRepository taskCommentRepository;
    private final TaskAttachmentRepository taskAttachmentRepository;
    private final ActivityLogService activityLogService;

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TaskResponse> getTasks(String search, String status, Long projectId, Long assignedUserId, int page, int size, String currentUserEmail) {
        User currentUser = userService.findByEmail(currentUserEmail);
        boolean isDeveloper = currentUser.getRoles().contains(Role.ROLE_DEVELOPER) && !currentUser.getRoles().contains(Role.ROLE_ADMIN);
        Pageable pageable = PageRequest.of(page, size);

        var tasks = taskRepository.findAll((root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();

            if (search != null && !search.isBlank()) {
                String term = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), term),
                        cb.like(cb.lower(root.get("description")), term),
                        cb.like(cb.lower(root.get("project").get("name")), term)
                ));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), Enum.valueOf(com.devops.dashboard.entity.TaskStatus.class, status)));
            }
            if (projectId != null) {
                predicates.add(cb.equal(root.get("project").get("id"), projectId));
            }
            if (assignedUserId != null) {
                predicates.add(cb.equal(root.get("assignedUser").get("id"), assignedUserId));
            }
            if (isDeveloper) {
                predicates.add(cb.equal(root.get("assignedUser").get("id"), currentUser.getId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return new PagedResponse<>(
                tasks.getContent().stream().map(this::toResponse).toList(),
                tasks.getNumber(),
                tasks.getSize(),
                tasks.getTotalElements(),
                tasks.getTotalPages()
        );
    }

    @Override
    public TaskResponse createTask(TaskRequest request, String actorEmail) {
        Task task = new Task();
        apply(task, request);
        Task savedTask = taskRepository.save(task);
        activityLogService.log(userService.findByEmail(actorEmail).getFullName(), "created", "Task", savedTask.getTitle());
        return toResponse(savedTask);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request, String actorEmail) {
        Task task = getTask(id);
        apply(task, request);
        Task savedTask = taskRepository.save(task);
        activityLogService.log(userService.findByEmail(actorEmail).getFullName(), "updated", "Task", savedTask.getTitle());
        return toResponse(savedTask);
    }

    @Override
    public void deleteTask(Long id, String actorEmail) {
        Task task = getTask(id);
        taskRepository.delete(task);
        activityLogService.log(userService.findByEmail(actorEmail).getFullName(), "deleted", "Task", task.getTitle());
    }

    @Override
    public TaskResponse updateStatus(Long id, TaskStatusUpdateRequest request, String actorEmail) {
        Task task = getTask(id);
        User actor = userService.findByEmail(actorEmail);
        boolean isAdmin = actor.getRoles().contains(Role.ROLE_ADMIN);
        if (!isAdmin && (task.getAssignedUser() == null || !task.getAssignedUser().getId().equals(actor.getId()))) {
            throw new ResourceNotFoundException("Task not found for the current user");
        }
        task.setStatus(request.status());
        Task savedTask = taskRepository.save(task);
        activityLogService.log(actor.getFullName(), "updated status", "Task", savedTask.getTitle());
        return toResponse(savedTask);
    }

    @Override
    public TaskCommentResponse addComment(Long taskId, CommentRequest request, String actorEmail) {
        Task task = getTask(taskId);
        User user = userService.findByEmail(actorEmail);
        TaskComment comment = new TaskComment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setContent(request.content());
        TaskComment savedComment = taskCommentRepository.save(comment);
        activityLogService.log(user.getFullName(), "commented on", "Task", task.getTitle());
        return new TaskCommentResponse(savedComment.getId(), savedComment.getContent(), user.getFullName(), savedComment.getCreatedAt());
    }

    @Override
    public TaskResponse uploadAttachment(Long taskId, MultipartFile file, String actorEmail) {
        Task task = getTask(taskId);
        try {
            Path uploadDir = Paths.get("uploads");
            Files.createDirectories(uploadDir);
            Path target = uploadDir.resolve(System.currentTimeMillis() + "-" + file.getOriginalFilename());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            TaskAttachment attachment = new TaskAttachment();
            attachment.setTask(task);
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileUrl(target.toString().replace("\\", "/"));
            taskAttachmentRepository.save(attachment);
            activityLogService.log(userService.findByEmail(actorEmail).getFullName(), "uploaded attachment to", "Task", task.getTitle());
        } catch (IOException exception) {
            throw new RuntimeException("Failed to upload attachment", exception);
        }
        return toResponse(task);
    }

    private void apply(Task task, TaskRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        User assignedUser = request.assignedUserId() == null ? null : userRepository.findById(request.assignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setStatus(request.status());
        task.setDeadline(request.deadline());
        task.setProject(project);
        task.setAssignedUser(assignedUser);
    }

    private Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    private TaskResponse toResponse(Task task) {
        var comments = taskCommentRepository.findByTaskIdOrderByCreatedAtAsc(task.getId()).stream()
                .map(comment -> new TaskCommentResponse(comment.getId(), comment.getContent(), comment.getUser().getFullName(), comment.getCreatedAt()))
                .toList();
        var attachments = taskAttachmentRepository.findByTaskId(task.getId()).stream()
                .map(attachment -> new TaskAttachmentResponse(attachment.getId(), attachment.getFileName(), attachment.getFileUrl(), attachment.getUploadedAt()))
                .toList();

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDeadline(),
                task.getProject().getId(),
                task.getProject().getName(),
                task.getAssignedUser() == null ? null : new UserSummary(task.getAssignedUser().getId(), task.getAssignedUser().getFullName(), task.getAssignedUser().getEmail()),
                task.getCreatedAt(),
                comments,
                attachments
        );
    }
}
