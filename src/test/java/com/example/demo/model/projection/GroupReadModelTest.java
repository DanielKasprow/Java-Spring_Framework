package com.example.demo.model.projection;

import com.example.demo.model.Task;
import com.example.demo.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

class GroupReadModelTest {
    @Test
    @DisplayName("should create null deadline for group when no task deadline")
    void constructor_noDeadlines_createsNullDeadline(){
        //given
        var source = new TaskGroup();
        source.setDescription("lorem");
        source.setTasks(Set.of(new Task("ipsum", null)));

        //when
        var result = new GroupReadModel(source);

        //then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }

}