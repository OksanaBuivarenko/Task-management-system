package com.effectivemobile.dto.request;

import com.effectivemobile.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @Schema(description = "Email", example = "ivanov@mail.ru")
    @NotNull
    @Email
    private String email;

    @Password(message = "Неверный формат пароля. Пароль должен состоять из букв, цифр и символов. " +
            "Обязательно содержать заглавную латинскую букву, цифру и иметь длину не менее 8 символов")
    @NotNull
    @Schema(description = "Password", example = "A1zxcvB8")
    private String password;
}
