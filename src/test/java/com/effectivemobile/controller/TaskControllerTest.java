package com.effectivemobile.controller;

import com.effectivemobile.TestContainers;
import com.effectivemobile.dto.request.PriorityRequest;
import com.effectivemobile.dto.request.StatusRequest;
import com.effectivemobile.dto.request.TaskRequest;
import com.effectivemobile.dto.request.UserRequest;
import com.effectivemobile.enumeration.TaskPriority;
import com.effectivemobile.enumeration.TaskStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestContainers.class)
@AutoConfigureMockMvc
class TaskControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Get all task with admin role user is success")
    @WithUserDetails(value = "alex@mail.ru")
    void getAllTasksWithAdminSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Test task 1"))
                .andExpect(jsonPath("$.data[2].author.id").value(2));
    }

    @Test
    @DisplayName("Get all task with user role is fail")
    @WithUserDetails(value = "anna@mail.ru")
    void getAllTasksWithUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks"))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @Test
    @DisplayName("Get all task with anonymous user is fail")
    @WithAnonymousUser
    void getAllTasksWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Get task by performer id with admin role is success")
    @WithUserDetails(value = "alex@mail.ru")
    void getAllTasksByPerformerWithAdminSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/performer/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Test task 1"))
                .andExpect(jsonPath("$.data[0].performer.id").value(1))
                .andExpect(jsonPath("$.data[1].title").value("Test task 2"))
                .andExpect(jsonPath("$.data[1].performer.id").value(1));
    }

    @Test
    @DisplayName("Get task by performer id with performer user is success")
    @WithUserDetails(value = "ivan@mail.ru")
    void getAllTasksByPerformerWithPerformerUserSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/performer/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Test task 1"))
                .andExpect(jsonPath("$.data[0].performer.id").value(1))
                .andExpect(jsonPath("$.data[1].title").value("Test task 2"))
                .andExpect(jsonPath("$.data[1].performer.id").value(1));
    }

    @Test
    @DisplayName("Get task by performer id with not performer user is fail")
    @WithUserDetails(value = "anna@mail.ru")
    void getAllTasksByPerformerWithNotPerformerUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/performer/1"))
                .andDo(print())
                .andExpectAll(status().isForbidden())
                .andExpect(jsonPath("$.error").value("User does not have access rights."));
    }

    @Test
    @DisplayName("Get task by performer id with anonymous user is fail")
    @WithAnonymousUser
    void getAllTasksByPerformerWithAnonimousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/performer/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Get task by id with admin role is success")
    @WithUserDetails(value = "alex@mail.ru")
    void getTasksWithAdminSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.title").value("Test task 1"))
                .andExpect(jsonPath("$.author.userName").value("Alexandr"))
                .andExpect(jsonPath("$.performer.userName").value("Ivan"))
                .andExpect(jsonPath("$.comments[0].text").value("Comment 1 text"))
                .andExpect(jsonPath("$.comments[1].text").value("Comment 3 text"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Get task by id with performer user is success")
    @WithUserDetails(value = "ivan@mail.ru")
    void getTasksByIdWithPerformerUserSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.title").value("Test task 1"))
                .andExpect(jsonPath("$.author.userName").value("Alexandr"))
                .andExpect(jsonPath("$.performer.userName").value("Ivan"))
                .andExpect(jsonPath("$.comments[0].text").value("Comment 1 text"))
                .andExpect(jsonPath("$.comments[1].text").value("Comment 3 text"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Get task by not present id with admin role is fail")
    @WithUserDetails(value = "alex@mail.ru")
    void getTasksByNotPresentIdWithAdminFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/100"))
                .andDo(print())
                .andExpectAll(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task with id 100 not found"));
    }

    @Test
    @DisplayName("Get task by id with not performer user is fail")
    @WithUserDetails(value = "anna@mail.ru")
    void getAllTasksByIdWithNotPerformerUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/1"))
                .andDo(print())
                .andExpectAll(status().isForbidden())
                .andExpect(jsonPath("$.error").value("User does not have access rights."));
    }

    @Test
    @DisplayName("Get task by id with anonymous user is fail")
    @WithAnonymousUser
    void getAllTasksByIdWithAnonimousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Search task by query with admin role is success")
    @WithUserDetails(value = "alex@mail.ru")
    void searchTasksWithAdminSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/search")
                        .param("title", "task")
                        .param("authorName", "Al")
                        .param("performer", "Ivan"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Test task 1"))
                .andExpect(jsonPath("$.data[0].author.userName").value("Alexandr"))
                .andExpect(jsonPath("$.data[0].performer.userName").value("Ivan"))
                .andExpect(jsonPath("$.data[0].comments[0].text").value("Comment 1 text"))
                .andExpect(jsonPath("$.data[0].comments[1].text").value("Comment 3 text"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2));
    }

    @Test
    @DisplayName("Search task by id query with admin role is success")
    @WithUserDetails(value = "alex@mail.ru")
    void searchTasksByIdWithAdminSuccess() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/search")
                        .param("id", "1")
                        .param("authorName", "Al"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Test task 1"))
                .andExpect(jsonPath("$.data[0].author.userName").value("Alexandr"))
                .andExpect(jsonPath("$.data[0].performer.userName").value("Ivan"))
                .andExpect(jsonPath("$.data[0].comments[0].text").value("Comment 1 text"))
                .andExpect(jsonPath("$.data[0].comments[1].text").value("Comment 3 text"))
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    @DisplayName("Search task by query with user role is fail")
    @WithUserDetails(value = "ivan@mail.ru")
    void searchTasksWithUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/search"))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @Test
    @DisplayName("Search task by query with anonymous user is fail")
    @WithAnonymousUser
    void searchTasksWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/tasks/search"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Create task with admin role is success")
    @WithUserDetails(value = "alex@mail.ru")
    void createTaskWithAdminSuccess() throws Exception {
        TaskRequest taskRequest = TaskRequest.builder()
                .title("New task")
                .description("To do new task")
                .authorId(2L)
                .performerId(3L)
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.PENDING)
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskRequest)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.title").value("New task"))
                .andExpect(jsonPath("$.author.userName").value("Alexandr"))
                .andExpect(jsonPath("$.performer.userName").value("Anna"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @DisplayName("Create task with already exists title with admin role is fail")
    @WithUserDetails(value = "alex@mail.ru")
    void createTaskWithAlreadyExistsTitleWithAdminFail() throws Exception {
        TaskRequest taskRequest = TaskRequest.builder()
                .title("Test task 1")
                .description("To do new task")
                .authorId(2L)
                .performerId(3L)
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.PENDING)
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskRequest)))
                .andDo(print())
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Task with title Test task 1 already exists"));
    }

    @Test
    @DisplayName("Create task with user role is fail")
    @WithUserDetails(value = "anna@mail.ru")
    void createTaskWithUserFail() throws Exception {
        TaskRequest taskRequest = TaskRequest.builder()
                .title("New task")
                .description("To do new task")
                .authorId(2L)
                .performerId(3L)
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.PENDING)
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskRequest)))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @Test
    @DisplayName("Create task with anonymous useris fail")
    @WithAnonymousUser
    void createTaskWithAnonymousUserFail() throws Exception {
        TaskRequest taskRequest = TaskRequest.builder()
                .title("New task")
                .description("To do new task")
                .authorId(2L)
                .performerId(3L)
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.PENDING)
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskRequest)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Update task with admin role is success")
    @WithUserDetails(value = "alex@mail.ru")
    void updateTaskWithAdminSuccess() throws Exception {
        TaskRequest taskRequest = TaskRequest.builder()
                .title("Update task")
                .description("To do update task")
                .authorId(2L)
                .performerId(3L)
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.PENDING)
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskRequest)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.title").value("Update task"))
                .andExpect(jsonPath("$.description").value("To do update task"))
                .andExpect(jsonPath("$.author.userName").value("Alexandr"))
                .andExpect(jsonPath("$.performer.userName").value("Anna"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Update task with user role is fail")
    @WithUserDetails(value = "anna@mail.ru")
    void updateTaskWithUserFail() throws Exception {
        TaskRequest taskRequest = TaskRequest.builder()
                .title("Update task")
                .description("To do update task")
                .authorId(2L)
                .performerId(3L)
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.PENDING)
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskRequest)))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @Test
    @DisplayName("Update task with anonymous user is fail")
    @WithAnonymousUser
    void updateTaskWithAnonymousUserFail() throws Exception {
        TaskRequest taskRequest = TaskRequest.builder()
                .title("Update task")
                .description("To do update task")
                .authorId(2L)
                .performerId(3L)
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.PENDING)
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskRequest)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Change status with admin role is success")
    @WithUserDetails(value = "alex@mail.ru")
    void changeTaskStatusWithAdminSuccess() throws Exception {
        StatusRequest statusRequest = StatusRequest.builder()
                .status(TaskStatus.PROGRESS)
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/tasks/status/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(statusRequest)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.title").value("Test task 1"))
                .andExpect(jsonPath("$.status").value("PROGRESS"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Change status with performer user is success")
    @WithUserDetails(value = "ivan@mail.ru")
    void changeTaskStatusWithPerformerUserSuccess() throws Exception {
        StatusRequest statusRequest = StatusRequest.builder()
                .status(TaskStatus.PROGRESS)
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/tasks/status/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(statusRequest)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.title").value("Test task 1"))
                .andExpect(jsonPath("$.status").value("PROGRESS"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Change status with not performer user is fail")
    @WithUserDetails(value = "anna@mail.ru")
    void changeTaskStatusWithNotPerformerUserFail() throws Exception {
        StatusRequest statusRequest = StatusRequest.builder()
                .status(TaskStatus.PROGRESS)
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/tasks/status/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(statusRequest)))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @Test
    @DisplayName("Change status with anonymous user is fail")
    @WithAnonymousUser
    void changeTaskStatusWithAnonymousUserFail() throws Exception {
        StatusRequest statusRequest = StatusRequest.builder()
                .status(TaskStatus.PROGRESS)
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/tasks/status/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(statusRequest)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Change priority with admin role is success")
    @WithUserDetails(value = "alex@mail.ru")
    void changeTaskPriorityWithAdminSuccess() throws Exception {
        PriorityRequest priorityRequest = PriorityRequest.builder()
                .priority(TaskPriority.LOW)
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/tasks/priority/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(priorityRequest)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.title").value("Test task 1"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Change priority with performer user is fail")
    @WithUserDetails(value = "ivan@mail.ru")
    void changeTaskPriorityWithPerformerUserFail() throws Exception {
        PriorityRequest priorityRequest = PriorityRequest.builder()
                .priority(TaskPriority.LOW)
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/tasks/priority/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(priorityRequest)))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @Test
    @DisplayName("Change priority with not performer user is fail")
    @WithUserDetails(value = "anna@mail.ru")
    void changeTaskPriorityWithNotPerformerUserFail() throws Exception {
        PriorityRequest priorityRequest = PriorityRequest.builder()
                .priority(TaskPriority.LOW)
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/tasks/priority/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(priorityRequest)))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @Test
    @DisplayName("Change priority with anonymous user is fail")
    @WithAnonymousUser
    void changeTaskPriorityWithAnonymousUserFail() throws Exception {
        PriorityRequest priorityRequest = PriorityRequest.builder()
                .priority(TaskPriority.LOW)
                .build();

        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/tasks/priority/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(priorityRequest)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Delete task with admin role is success")
    @WithUserDetails(value = "alex@mail.ru")
    void deleteTaskWithAdminSuccess() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/tasks/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.message").value("Task with id 1 delete success"));
    }

    @Test
    @DisplayName("Delete task with user role is fail")
    @WithUserDetails(value = "ivan@mail.ru")
    void deleteTaskWithUserFail() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/tasks/1"))
                .andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @Test
    @DisplayName("Delete task with anonymous user is fail")
    @WithAnonymousUser
    void deleteTaskWithAnonymousUserFail() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/tasks/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }
}