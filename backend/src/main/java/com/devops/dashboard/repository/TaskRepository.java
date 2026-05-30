package com.devops.dashboard.repository;

import com.devops.dashboard.entity.Task;
import com.devops.dashboard.entity.TaskStatus;
import com.devops.dashboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @EntityGraph(attributePaths = {"project", "assignedUser"})
    Page<Task> findAll(Pageable pageable);

    long countByStatus(TaskStatus status);

    long countByAssignedUser(User user);
}
