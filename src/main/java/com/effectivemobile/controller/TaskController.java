package com.effectivemobile.controller;

import com.effectivemobile.dto.request.*;
import com.effectivemobile.dto.response.MessageResponse;
import com.effectivemobile.dto.response.PageResponse;
import com.effectivemobile.dto.response.TaskResponse;
import com.effectivemobile.security.AppUserDetails;
import com.effectivemobile.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SecurityRequirement(name = "Bearer Authentication")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Getting all tasks")
    @GetMapping()
    public PageResponse<List<TaskResponse>> getAllTasks(@ParameterObject @Valid PageSizeRequest req) {
        return taskService.getAllTasks(req.getOffset(), req.getLimit());
    }

    @Operation(description = "Getting all tasks by performer id")
    @GetMapping("/performer/{id}")
    public PageResponse<List<TaskResponse>> getAllTasksByPerformer(@PathVariable Long id,
                                                                   @ParameterObject @Valid PageSizeRequest req,
                                                                   @AuthenticationPrincipal AppUserDetails userDetails) {
        return taskService.getAllTasksByPerformer(id, req.getOffset(), req.getLimit(), userDetails.getUsername());
    }

    @Operation(description = "Getting task by id")
    @GetMapping(value = "/{id}")
    public TaskResponse getTask(@PathVariable Long id,
                                @AuthenticationPrincipal AppUserDetails userDetails) {
        return taskService.getTaskResponse(id, userDetails.getUsername());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Searching tasks by query")
    @GetMapping(value = "/search")
    public PageResponse<List<TaskResponse>> searchTasks(@ParameterObject @Valid TaskSearchRequest searchRq,
                                                        @ParameterObject @Valid PageSizeRequest req) {
        return taskService.searchTask(searchRq, req.getOffset(), req.getLimit());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Create task")
    @PostMapping()
    public TaskResponse createTask(@RequestBody @Valid TaskRequest taskRq) {
        return taskService.createTask(taskRq);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Update task by id")
    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRq) {
        return taskService.updateTask(id, taskRq);
    }

    @Operation(description = "Change task's status by id")
    @PutMapping("/status/{id}")
    public TaskResponse changeTaskStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest statusRq,
                                         @AuthenticationPrincipal AppUserDetails userDetails) {
        return taskService.changeTaskStatus(id, statusRq, userDetails.getUsername());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Change task's priority by id")
    @PutMapping("/priority/{id}")
    public TaskResponse changeTaskPriority(@PathVariable Long id, @Valid @RequestBody PriorityRequest priorityRq) {
        return taskService.changeTaskPriority(id, priorityRq);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Delete task by id")
    @DeleteMapping("/{id}")
    public MessageResponse deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }
}
