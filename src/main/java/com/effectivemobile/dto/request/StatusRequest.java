package com.effectivemobile.dto.request;

import com.effectivemobile.enumeration.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusRequest {

    @Schema(description = "Task status enum: PENDING, PROGRESS, COMPLETED", example = "PENDING")
    @NotNull
    private TaskStatus status;
}
