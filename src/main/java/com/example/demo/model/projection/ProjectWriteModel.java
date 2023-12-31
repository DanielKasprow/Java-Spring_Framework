package com.example.demo.model.projection;

import com.example.demo.model.Project;
import com.example.demo.model.ProjectSteps;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {
    @NotBlank(message = "Project's description must not be empty")
    private String description;
    @Valid
    private List<ProjectSteps> steps = new ArrayList<>();

    public ProjectWriteModel(){
        steps.add(new ProjectSteps());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectSteps> getSteps() {
        return steps;
    }

    public void setSteps(List<ProjectSteps> steps) {
        this.steps = steps;
    }

    public Project toProject(){
        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }
}