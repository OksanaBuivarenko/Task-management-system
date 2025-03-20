package com.effectivemobile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PageSizeRequest {
    @Schema(example = "Zero-based page index(0...N)")
    @Min(value = 0)
    private Integer offset = 0;

    @Schema(example = "The size of the page to be returned")
    @Positive
    private Integer limit = 20;
}
