package com.example.demo.logic;

import com.example.demo.TaskConfigurationProperties;
import com.example.demo.model.ProjectRepository;
import com.example.demo.model.TaskGroupRepository;
import com.example.demo.model.TaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ImportResource("classpath:applicationContext.xml")
public class LogicConfiguration {
    @Bean
    ProjectService projectService(
            final ProjectRepository projectRepository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfigurationProperties properties,
            final TaskGroupService taskGroupService
    ) {
        return new ProjectService(projectRepository, taskGroupRepository, properties, taskGroupService);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository taskGroupRepository,
            @Qualifier("sqlTaskRepository") final TaskRepository taskRepository
    ) {
        return new TaskGroupService(taskGroupRepository, taskRepository);
    }
}
