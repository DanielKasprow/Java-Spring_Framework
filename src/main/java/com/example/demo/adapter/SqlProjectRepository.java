package com.example.demo.adapter;

import com.example.demo.model.Project;
import com.example.demo.model.ProjectRepository;
import com.example.demo.model.TaskGroup;
import com.example.demo.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {
    @Override
    @Query("select distinct p FROM Project p join fetch p.steps")
    List<Project> findAll();
}
