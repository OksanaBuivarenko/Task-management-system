package com.effectivemobile.dto.response;

import com.effectivemobile.enumeration.TaskPriority;
import com.effectivemobile.enumeration.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class TaskResponse {

    @Schema(example = "4567")
    private Long id;

    @Schema(description = "Task title", example = "JAVA Test task")
    private String title;

    @Schema(description = "Task description", example = "It is necessary to develop a simple task management system")
    private String description;

    @Schema(description = "Task status enum: PENDING, PROGRESS, COMPLETED", example = "PENDING")
    private TaskStatus status;

    @Schema(description = "Task priority enum: HIGH, MEDIUM, LOW", example = "MEDIUM")
    private TaskPriority priority;

    @Schema(description = "List of comments to the task")
    private List<CommentResponse> comments;

    @Schema(description = "Task author")
    private UserResponse author;

    @Schema(description = "Task performer")
    private UserResponse performer;
}
