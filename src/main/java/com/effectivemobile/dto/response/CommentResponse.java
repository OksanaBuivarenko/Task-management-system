package com.effectivemobile.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {

    @Schema(description = "Comment id", example = "2")
    private Long id;

    @Schema(description = "Comment's author")
    private UserResponse author;

    @Schema(description = "Comment text", example = "Complete by 20.03.2025")
    private String text;

    @Schema(description = "Comment create time", example = "20.03.2025 14:05:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime time;
}
