package com.effectivemobile.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorResponse {

    @Schema(description = "Error code", example = "404")
    private HttpStatus status;

    @Schema(description = "Error message", example = "Company not found")
    private String error;
}
