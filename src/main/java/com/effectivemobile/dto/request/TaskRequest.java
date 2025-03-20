package com.effectivemobile.dto.request;

import com.effectivemobile.enumeration.TaskPriority;
import com.effectivemobile.enumeration.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRequest {

    @Schema(description = "Task title", example = "JAVA Test task")
    @NotBlank
    private String title;

    @Schema(description = "Task description", example = "It is necessary to develop a simple task management system")
    @NotBlank
    private String description;

    @Schema(description = "Task status enum: PENDING, PROGRESS, COMPLETED", example = "PENDING")
    @NotNull
    private TaskStatus status;

    @Schema(description = "Task priority enum: HIGH, MEDIUM, LOW", example = "MEDIUM")
    private TaskPriority priority;

    @Schema(description = "Task author id", example = "15")
    @NotNull
    @Positive
    private Long authorId;

    @Schema(description = "Task performer id", example = "15")
    @NotNull
    @Positive
    private Long performerId;
}
