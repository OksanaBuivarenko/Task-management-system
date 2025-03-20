package com.effectivemobile.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponse {

    @Schema(description = "Message", example = "Task with id 1 delete success")
    private String message;
}
