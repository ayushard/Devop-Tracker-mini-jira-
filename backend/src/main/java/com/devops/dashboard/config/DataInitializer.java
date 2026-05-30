package com.devops.dashboard.config;

import com.devops.dashboard.entity.Project;
import com.devops.dashboard.entity.Role;
import com.devops.dashboard.entity.Task;
import com.devops.dashboard.entity.TaskPriority;
import com.devops.dashboard.entity.TaskStatus;
import com.devops.dashboard.entity.User;
import com.devops.dashboard.repository.ProjectRepository;
import com.devops.dashboard.repository.TaskRepository;
import com.devops.dashboard.repository.UserRepository;
import java.time.LocalDate;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = new User();
        admin.setFullName("Ava Admin");
        admin.setEmail("admin@devops.local");
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setRoles(Set.of(Role.ROLE_ADMIN));

        User devOne = new User();
        devOne.setFullName("Diego Developer");
        devOne.setEmail("developer1@devops.local");
        devOne.setPassword(passwordEncoder.encode("Dev@123"));
        devOne.setRoles(Set.of(Role.ROLE_DEVELOPER));

        User devTwo = new User();
        devTwo.setFullName("Priya Engineer");
        devTwo.setEmail("developer2@devops.local");
        devTwo.setPassword(passwordEncoder.encode("Dev@123"));
        devTwo.setRoles(Set.of(Role.ROLE_DEVELOPER));

        userRepository.save(admin);
        userRepository.save(devOne);
        userRepository.save(devTwo);

        Project platform = new Project();
        platform.setName("Cloud Platform Modernization");
        platform.setDescription("Upgrade deployment pipelines, observability, and release process.");
        platform.setKeyCode("CPM");
        platform.setStartDate(LocalDate.now().minusDays(20));
        platform.setEndDate(LocalDate.now().plusDays(45));

        Project infra = new Project();
        infra.setName("Infrastructure Cost Optimization");
        infra.setDescription("Reduce cloud spend through rightsizing and automation.");
        infra.setKeyCode("ICO");
        infra.setStartDate(LocalDate.now().minusDays(10));
        infra.setEndDate(LocalDate.now().plusDays(30));

        projectRepository.save(platform);
        projectRepository.save(infra);

        taskRepository.save(buildTask("Harden Kubernetes manifests", "Review probes, quotas, and pod security policies.", TaskPriority.HIGH, TaskStatus.IN_PROGRESS, platform, devOne, 7));
        taskRepository.save(buildTask("Add deployment rollback playbook", "Document rollback automation and release communication.", TaskPriority.MEDIUM, TaskStatus.TODO, platform, devTwo, 10));
        taskRepository.save(buildTask("Audit idle resources", "Identify unattached storage and underutilized workloads.", TaskPriority.HIGH, TaskStatus.COMPLETED, infra, devOne, 3));
        taskRepository.save(buildTask("Automate cost anomaly alerts", "Integrate budget notifications into Slack.", TaskPriority.MEDIUM, TaskStatus.TODO, infra, devTwo, 12));
    }

    private Task buildTask(String title, String description, TaskPriority priority, TaskStatus status, Project project, User assignedUser, int daysUntilDeadline) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(status);
        task.setProject(project);
        task.setAssignedUser(assignedUser);
        task.setDeadline(LocalDate.now().plusDays(daysUntilDeadline));
        return task;
    }
}
