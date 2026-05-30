package com.devops.dashboard.service;

import com.devops.dashboard.dto.common.PagedResponse;
import com.devops.dashboard.dto.project.ProjectRequest;
import com.devops.dashboard.dto.project.ProjectResponse;

public interface ProjectService {
    PagedResponse<ProjectResponse> getProjects(String search, int page, int size);
    ProjectResponse createProject(ProjectRequest request, String actorEmail);
    ProjectResponse updateProject(Long id, ProjectRequest request, String actorEmail);
    void deleteProject(Long id, String actorEmail);
}
