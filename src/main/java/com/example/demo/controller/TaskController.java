package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.TaskRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/tasks")
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;

    TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //@RequestMapping(method = RequestMethod.GET, path = "/tasks")

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate) {
        Task result = taskRepository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }


    @GetMapping(params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @RequestMapping( method = RequestMethod.GET)
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(taskRepository.findAll(page).getContent());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(
                taskRepository.findByDone(state)
        );
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    taskRepository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> taggleTask(@PathVariable int id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }


}
