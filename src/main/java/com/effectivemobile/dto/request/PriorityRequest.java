package com.effectivemobile.dto.request;

import com.effectivemobile.enumeration.TaskPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriorityRequest {

    @Schema(description = "Task priority enum: HIGH, MEDIUM, LOW", example = "MEDIUM")
    private TaskPriority priority;
}
