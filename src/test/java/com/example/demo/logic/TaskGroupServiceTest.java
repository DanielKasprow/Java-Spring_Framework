package com.example.demo.logic;

import com.example.demo.model.TaskGroup;
import com.example.demo.model.TaskGroupRepository;
import com.example.demo.model.TaskRepository;
import com.example.demo.model.projection.GroupReadModel;
import com.example.demo.model.projection.GroupTaskReadModel;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    void toggleGroup_existsReturnTrue() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturn(true);
        //system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);

        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone tasks");

    }

    @Test
    void toggleGroup_findReturnOptional() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturn(false);
        //and
        TaskGroupRepository mockGroupRepository = groupRepositoryReturn();
        //system under test
        var toTest = new TaskGroupService(mockGroupRepository,mockTaskRepository);
        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(anyInt()));
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }
    @Test
    void toggleGroup_succes() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturn(false);

        //and
        var group = new TaskGroup();
        var beforeToggle = group.isDone();

        //and
        TaskGroupRepository mockGroupRepository = groupRepositoryReturn();
        when(mockGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));

        //system under test
        var toTest = new TaskGroupService(mockGroupRepository,mockTaskRepository);

        //when
        toTest.toggleGroup(0);

        //then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);

    }
    private static TaskRepository taskRepositoryReturn(boolean t) {
        var mockGroupRepository = mock(TaskRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(t);
        return mockGroupRepository;
    }
    private static TaskGroupRepository groupRepositoryReturn() {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        return mockGroupRepository;
    }
}