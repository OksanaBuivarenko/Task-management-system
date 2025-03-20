package com.effectivemobile.controller;

import com.effectivemobile.TestContainers;
import com.effectivemobile.dto.request.CommentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestContainers.class)
@AutoConfigureMockMvc
@WithMockUser
class CommentControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithUserDetails(value = "ivan@mail.ru")
    @DisplayName("Create new comment with current credentials is success")
    void createCommentWithCurrentCredentialsSuccess() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        CommentRequest commentRequest = CommentRequest.builder()
                .authorId(1L)
                .text("Test comment text")
                .time(LocalDateTime.now())
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/comments/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.text").value("Test comment text"))
                .andExpect(jsonPath("$.author.userName").value("Ivan"))
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @WithUserDetails(value = "alex@mail.ru")
    @DisplayName("Create new comment with admin is success")
    void createCommentWithAdminSuccess() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        CommentRequest commentRequest = CommentRequest.builder()
                .authorId(2L)
                .text("Test comment text")
                .time(LocalDateTime.now())
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/comments/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.text").value("Test comment text"))
                .andExpect(jsonPath("$.author.userName").value("Alexandr"))
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @WithUserDetails(value = "anna@mail.ru")
    @DisplayName("Create new comment with not performer user is fail")
    void createCommentWithNotPerformerUserFail() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        CommentRequest commentRequest = CommentRequest.builder()
                .authorId(3L)
                .text("Test comment text")
                .time(LocalDateTime.now())
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/comments/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpectAll(status().isForbidden())
                .andExpect(jsonPath("$.error").value("User does not have access rights."));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Create new comment anonymous user is fail")
    void createCommentWithAnonymousUserFail() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .authorId(1L)
                .text("Test comment text")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/comments/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Create new comment with bad credentials is fail")
    void createCommentWithBadCredentialsFail() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder()
                .text("")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/comments/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Not valid fields"))
                .andExpect(jsonPath("$.params.text").value("must not be blank"))
                .andExpect(jsonPath("$.params.authorId").value("must not be null"));
    }
}