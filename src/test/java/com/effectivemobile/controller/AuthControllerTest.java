package com.effectivemobile.controller;

import com.effectivemobile.TestContainers;
import com.effectivemobile.dto.request.LoginRequest;
import com.effectivemobile.dto.request.UserRequest;
import com.effectivemobile.enumeration.RoleType;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestContainers.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Login with current credentials is success")
    void authUserWithCurrentCredentialsSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("ivan@mail.ru")
                .password("A1234567")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.userName").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@mail.ru"));
    }

    @Test
    @DisplayName("Login with incorrect password is fail")
    void authUserWithIncorrectPasswordFail() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("ivan@mail.ru")
                .password("A7654321")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login with incorrect email is fail")
    void authUserWithIncorrectEmailFail() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("ivanov@mail.ru")
                .password("A7654321")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    @DisplayName("Register new user with current credentials is success")
    void registerUserWithCurrentCredentialsSuccess() throws Exception {
        Set<RoleType> roles = new HashSet<>();
        roles.add(RoleType.ROLE_USER);
        UserRequest userRequest = UserRequest.builder()
                .userName("Petr")
                .roles(roles)
                .email("petr@mail.ru")
                .password("P1234567")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.userName").value("Petr"))
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @DisplayName("Register new user with already exists email is fail")
    void registerUserWithAlreadyExistsEmailFail() throws Exception {
        Set<RoleType> roles = new HashSet<>();
        roles.add(RoleType.ROLE_USER);
        UserRequest userRequest = UserRequest.builder()
                .userName("Petr")
                .roles(roles)
                .email("ivan@mail.ru")
                .password("A1234567")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email already exists!"));
    }

    @Test
    @DisplayName("Register new user with already exists user name is fail")
    void registerUserWithAlreadyExistsNameFail() throws Exception {
        Set<RoleType> roles = new HashSet<>();
        roles.add(RoleType.ROLE_USER);
        UserRequest userRequest = UserRequest.builder()
                .userName("Ivan")
                .roles(roles)
                .email("ivanov@mail.ru")
                .password("A1234567")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User name already exists!"));
    }

    @Test
    @DisplayName("Register new user with bad credentials is fail")
    void registerUserWithBadCredentialsFail() throws Exception {
        Set<RoleType> roles = new HashSet<>();
        roles.add(RoleType.ROLE_USER);
        UserRequest userRequest = UserRequest.builder()
                .userName("Petr")
                .roles(roles)
                .email("petrmailru")
                .password("01234567")
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpectAll(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Not valid fields"))
                .andExpect(jsonPath("$.params.email").value("must be a well-formed email address"))
                .andExpect(jsonPath("$.params.password").value("Incorrect password format." +
                        " Password must contain letters, numbers and symbols. Be sure to keep a capital Latin letter," +
                        " number and be at least 8 characters long"));
    }

    @Test
    @DisplayName("Logout authenticated user is success")
    @WithMockUser
    void logoutIsAuthenticatedUserSuccess() throws Exception {
        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/logout"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.message").value("User logout!"));
    }

    @Test
    @DisplayName("Logout anonymous user is fail")
    @WithAnonymousUser
    void logoutIsAnonymousUserFail() throws Exception {
        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/logout"))
                .andDo(print())
                .andExpectAll(status().isForbidden() );
    }
}