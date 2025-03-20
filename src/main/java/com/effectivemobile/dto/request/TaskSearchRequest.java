package com.effectivemobile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TaskSearchRequest {

    @Schema(description = "Task id", example = "4567")
    private Long id;

    @Schema(description = "Task title", example = "JAVA Test task")
    private String title;

    @Schema(description = "Task author name", example = "Ivan")
    private String authorName;

    @Schema(description = "Task performer name", example = "Ivan")
    private String performerName;
}
