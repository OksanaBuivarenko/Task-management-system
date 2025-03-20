package com.effectivemobile.mapper;

import com.effectivemobile.dto.request.TaskRequest;
import com.effectivemobile.dto.response.TaskResponse;
import com.effectivemobile.entity.Task;
import com.effectivemobile.entity.User;
import com.effectivemobile.enumeration.TaskPriority;
import com.effectivemobile.enumeration.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private TaskMapper taskMapper;

    @BeforeEach
    public void setUp() {
        taskMapper = Mappers.getMapper(TaskMapper.class);
    }

    @Test
    void toDto() {
        User author = new User();
        author.setId(1L);

        User performer = new User();
        performer.setId(2L);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("To do test task.");
        task.setPriority(TaskPriority.LOW);
        task.setStatus(TaskStatus.PENDING);
        task.setAuthor(author);
        task.setPerformer(performer);

        TaskResponse taskResponse = taskMapper.toDto(task);

        assertEquals(task.getId(), taskResponse.getId());
        assertEquals(task.getTitle(), taskResponse.getTitle());
        assertEquals(task.getDescription(), taskResponse.getDescription());
        assertEquals(task.getPriority(), taskResponse.getPriority());
        assertEquals(task.getStatus(), taskResponse.getStatus());
        assertEquals(task.getAuthor().getId(), taskResponse.getAuthor().getId());
        assertEquals(task.getPerformer().getId(), taskResponse.getPerformer().getId());
    }

    @Test
    void testToNullDto() {
        TaskResponse taskResponse = taskMapper.toDto(null);
        assertNull(taskResponse);
    }

    @Test
    void toEntity() {
        TaskRequest taskRequest = TaskRequest.builder()
                        .title("Test task")
                        .description("Do test task")
                        .priority(TaskPriority.LOW)
                        .status(TaskStatus.PENDING)
                        .authorId(1L)
                        .performerId(1L)
                        .build();

        User author = new User();
        author.setId(1L);

        User performer = new User();
        performer.setId(2L);

        Task task = taskMapper.toEntity(taskRequest, author, performer);

        assertEquals(taskRequest.getTitle(), task.getTitle());
        assertEquals(taskRequest.getDescription(), task.getDescription());
        assertEquals(author, task.getAuthor());
        assertEquals(performer, task.getPerformer());
        assertEquals(null, task.getId());
        assertEquals(taskRequest.getPriority(), task.getPriority());
        assertEquals(taskRequest.getStatus(), task.getStatus());
    }

    @Test
    void toNullEntity() {

        Task task = taskMapper.toEntity(null, null, null);
        assertNull(task);
    }
}