package com.example.demo.logic;

import com.example.demo.model.Project;
import com.example.demo.model.TaskGroup;
import com.example.demo.model.TaskGroupRepository;
import com.example.demo.model.TaskRepository;
import com.example.demo.model.projection.GroupReadModel;
import com.example.demo.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

//@Service
//@RequestScope
public class TaskGroupService {
    private TaskGroupRepository taskGroupRepository;
    private TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.taskGroupRepository = repository;
        this.taskRepository = taskRepository;
    }

    GroupReadModel createGroup(final GroupWriteModel source, final Project project) {
        TaskGroup result = taskGroupRepository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return taskGroupRepository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId){
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)){
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        }
        TaskGroup result = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Task group with given id not found"));
        result.setDone(!result.isDone());
        taskGroupRepository.save(result);
    }

}
