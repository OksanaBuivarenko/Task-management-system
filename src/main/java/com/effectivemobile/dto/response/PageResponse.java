package com.effectivemobile.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PageResponse<T> {

    @Builder.Default
    @Schema(description = "Current time", example = "20.03.2025 14:05:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime time = LocalDateTime.now();

    @Schema(example = "Collection of objects or just object any type")
    private T data;

    @Schema(example = "0")
    private Integer offset;

    @Schema(example = "20")
    private Integer limit;
}
