package com.effectivemobile.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserResponse {

    @Schema(description = "Person id", example = "365")
    private Long id;

    @Schema(description = "Person username", example = "Ivan")
    private String userName;
}
