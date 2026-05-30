package com.devops.dashboard.service.impl;

import com.devops.dashboard.dto.common.PagedResponse;
import com.devops.dashboard.dto.project.ProjectRequest;
import com.devops.dashboard.dto.project.ProjectResponse;
import com.devops.dashboard.entity.Project;
import com.devops.dashboard.entity.Task;
import com.devops.dashboard.entity.TaskStatus;
import com.devops.dashboard.exception.ResourceNotFoundException;
import com.devops.dashboard.repository.ProjectRepository;
import com.devops.dashboard.repository.TaskRepository;
import com.devops.dashboard.service.ActivityLogService;
import com.devops.dashboard.service.ProjectService;
import com.devops.dashboard.service.UserService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ActivityLogService activityLogService;

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProjectResponse> getProjects(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var projects = projectRepository.findAll((root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (search != null && !search.isBlank()) {
                String term = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), term),
                        cb.like(cb.lower(root.get("description")), term),
                        cb.like(cb.lower(root.get("keyCode")), term)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return new PagedResponse<>(
                projects.getContent().stream().map(this::toResponse).toList(),
                projects.getNumber(),
                projects.getSize(),
                projects.getTotalElements(),
                projects.getTotalPages()
        );
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, String actorEmail) {
        Project project = new Project();
        apply(project, request);
        Project savedProject = projectRepository.save(project);
        activityLogService.log(userService.findByEmail(actorEmail).getFullName(), "created", "Project", savedProject.getName());
        return toResponse(savedProject);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, String actorEmail) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        apply(project, request);
        Project savedProject = projectRepository.save(project);
        activityLogService.log(userService.findByEmail(actorEmail).getFullName(), "updated", "Project", savedProject.getName());
        return toResponse(savedProject);
    }

    @Override
    public void deleteProject(Long id, String actorEmail) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        projectRepository.delete(project);
        activityLogService.log(userService.findByEmail(actorEmail).getFullName(), "deleted", "Project", project.getName());
    }

    private void apply(Project project, ProjectRequest request) {
        project.setName(request.name());
        project.setDescription(request.description());
        project.setKeyCode(request.keyCode());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
    }

    private ProjectResponse toResponse(Project project) {
        long totalTasks = taskRepository.count((root, query, cb) -> cb.equal(root.get("project").get("id"), project.getId()));
        long completedTasks = taskRepository.count((root, query, cb) -> cb.and(
                cb.equal(root.get("project").get("id"), project.getId()),
                cb.equal(root.get("status"), TaskStatus.COMPLETED)
        ));
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getKeyCode(),
                project.getStartDate(),
                project.getEndDate(),
                project.getCreatedAt(),
                totalTasks,
                completedTasks
        );
    }
}
