package com.example.demo.logic;

import com.example.demo.model.Task;
import com.example.demo.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;

    public TaskService(@Qualifier("sqlTaskRepository") TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Async
    public CompletableFuture<List<Task>> findAllAsync(){
        logger.info("Async!");
        return CompletableFuture.supplyAsync(() -> taskRepository.findAll());
    }
}
