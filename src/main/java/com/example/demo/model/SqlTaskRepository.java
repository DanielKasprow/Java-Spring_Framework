package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository//RestResource(path = "todos", collectionResourceRel = "todos")
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    /*@Override
    @RestResource(exported = false)
    void deleteById(Integer integer);

    @Override
    @RestResource(exported = false)
    void delete(Task entity);*/
    //@RestResource(path = "done", rel = "done")
}
