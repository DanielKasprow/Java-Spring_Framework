package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository repo;
    @Test
    void httpGet_returnsGivenTask() throws Exception {

        // given
        int id = repo.save(new Task("lorem", LocalDateTime.now())).getId();

        // when + then
        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    void httpGet_returnsFirstTask() throws Exception {
        // given
        List<Task> tasks  = new ArrayList<>();
        repo.save(new Task("lorem", LocalDateTime.now()));
        // when
        List<Task> result = repo.findAll();
        // then
        mockMvc.perform(get("/tasks/" + result.get(0).getId()))
                .andExpect(status().is2xxSuccessful());
    }
}