package com.effectivemobile.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class AuthResponse {

    @Schema(description = "User id", example = "135")
    private Long id;

    @Schema(description = "Token")
    private String token;

    @Schema(description = "Refresh token")
    private String refreshToken;

    @Schema(description = "User name", example = "Ivan")
    private String userName;

    @Schema(description = "Email", example = "ivanov@mail.ru")
    private String email;

    @Schema(description = "Set roles", example = "[\"ROLE_USER\"]")
    private List<String> roles;
}
