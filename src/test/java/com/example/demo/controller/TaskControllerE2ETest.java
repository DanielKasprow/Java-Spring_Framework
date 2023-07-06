package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    TaskRepository repo;

    @Test
    void httpGet_returnsAllTasks(){
        // given
        int initial = repo.findAll().size();
        repo.save(new Task("lorem", LocalDateTime.now()));
        repo.save(new Task("ipsum", LocalDateTime.now()));
        // when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);
        // then
        assertThat(result).hasSize(initial + 2);
    }


}