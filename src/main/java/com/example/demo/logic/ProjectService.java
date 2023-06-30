package com.example.demo.logic;

import com.example.demo.TaskConfigurationProperties;
import com.example.demo.model.*;
import com.example.demo.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskGroupRepository groupRepository;
    private TaskConfigurationProperties properties;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository groupRepository, TaskConfigurationProperties properties) {
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
        this.properties = properties;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final Project toSave) {
        return projectRepository.save(toSave);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (!properties.getTemplate().isAllowMultipleTasks() && groupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("ONly one undone group is allowed");
        }
        TaskGroup result = projectRepository.findById(projectId).map(project -> {
            var targetGroup = new TaskGroup();
            targetGroup.setDescription(project.getDescription());
            targetGroup.setTasks(
                    project.getSteps().stream()
                            .map(steps -> new Task(
                                            steps.getDescription(),
                                            deadline.plusDays(steps.getDays_to_deadline())
                                    )
                            ).collect(Collectors.toSet()));
            return targetGroup;
        }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);

    }


}
