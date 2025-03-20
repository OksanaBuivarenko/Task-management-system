package com.effectivemobile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentRequest {

    @Schema(description = "Comment author id", example = "365")
    @NotNull
    private Long authorId;

    @Schema(description = "Comment text", example = "Complete by 20.03.2025")
    @NotBlank
    private String text;

    @Schema(description = "Comment create time", example = "20.03.2025 14:05:00")
    @NotNull
    private LocalDateTime time;
}
