package com.effectivemobile.dto.request;

import com.effectivemobile.enumeration.RoleType;
import com.effectivemobile.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Set;


@Data
@Builder
public class UserRequest {

    @Schema(description = "User name", example = "Ivan")
    @NotBlank
    private String userName;

    @Schema(description = "Email", example = "ivanov@mail.ru")
    @NotNull
    @Email
    private String email;

    @Schema(description = "Set roles", example = "[\"ROLE_USER\"]")
    @NotNull
    private Set<RoleType> roles;

    @Password(message = "Incorrect password format. Password must contain letters, numbers and symbols. Be sure " +
            "to keep a capital Latin letter, number and be at least 8 characters long")
    @NotNull
    @Schema(description = "Password", example = "A1zxcvB8")
    private String password;
}
