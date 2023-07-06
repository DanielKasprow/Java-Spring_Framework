package com.example.demo.logic;

import com.example.demo.TaskConfigurationProperties;
import com.example.demo.model.*;
import com.example.demo.model.projection.GroupReadModel;
import com.example.demo.model.projection.GroupTaskWriteModel;
import com.example.demo.model.projection.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Service
//@RequestScope
public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskGroupRepository groupRepository;
    private TaskConfigurationProperties properties;
    private TaskGroupService taskGroupService;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository groupRepository, TaskConfigurationProperties properties, TaskGroupService taskGroupService) {
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
        this.properties = properties;
        this.taskGroupService = taskGroupService;
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
        GroupReadModel result = projectRepository.findById(projectId).map(project -> {
            var targetGroup = new GroupWriteModel();
            targetGroup.setDescription(project.getDescription());
            targetGroup.setTasks(
                    project.getSteps().stream()
                            .map(steps -> {
                                        var task = new GroupTaskWriteModel();
                                        task.setDescription(steps.getDescription());
                                        task.setDeadline(deadline.plusDays(steps.getDaysToDeadline()));
                                        return task;
                                    }
                            ).collect(Collectors.toSet())
            );
            return taskGroupService.createGroup(targetGroup, project);
        }).orElseThrow(() -> new IllegalArgumentException("id not Found"));
        return result;

    }


}
