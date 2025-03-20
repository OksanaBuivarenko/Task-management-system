package com.effectivemobile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @Schema(description = "Refresh token")
    private String refreshToken;
}
