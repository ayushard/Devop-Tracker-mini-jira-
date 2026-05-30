package com.devops.dashboard.repository;

import com.devops.dashboard.entity.TaskAttachment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, Long> {
    List<TaskAttachment> findByTaskId(Long taskId);
}
