package com.example.demo.logic;

import com.example.demo.TaskConfigurationProperties;
import com.example.demo.model.*;
import com.example.demo.model.projection.GroupReadModel;
import com.example.demo.model.projection.GroupTaskWriteModel;
import com.example.demo.model.projection.GroupWriteModel;
import com.example.demo.model.projection.ProjectWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Service
//@RequestScope
public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskGroupRepository groupRepository;
    private TaskConfigurationProperties taskConfigurationProperties;
    private TaskGroupService taskGroupService;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository groupRepository, TaskConfigurationProperties taskConfigurationProperties, TaskGroupService taskGroupService) {
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
        this.taskConfigurationProperties = taskConfigurationProperties;
        this.taskGroupService = taskGroupService;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadModel createGroup( int projectId, LocalDateTime deadline) {
        if (!taskConfigurationProperties.getTemplate().isAllowMultipleTasks() && groupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                                var task = new GroupTaskWriteModel();
                                                task.setDescription(projectStep.getDescription());
                                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                                return task;
                                            }
                                    ).collect(Collectors.toList())
                    );
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }


}
