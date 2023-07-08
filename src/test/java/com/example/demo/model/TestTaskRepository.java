package com.example.demo.model;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;


public class TestTaskRepository implements TaskRepository{

    private Map<Integer, Task> tasks = new HashMap<>();

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Page<Task> findAll(Pageable page) {
        return null;
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public boolean existsById(Integer id) {
        return tasks.containsKey(id);
    }

    @Override
    public boolean existsByDoneIsFalseAndGroup_Id(Integer groupId) {
        return false;
    }

    @Override
    public List<Task> findByDone(boolean done) {
        return null;
    }

    @Override
    public Task save(Task entity) {
        return tasks.put(tasks.size() +1, entity);
    }

    @Override
    public List<Task> findAllByGroup_Id(Integer id) {
        return List.of();
    }
}
