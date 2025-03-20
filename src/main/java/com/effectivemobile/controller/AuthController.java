package com.effectivemobile.controller;

import com.effectivemobile.dto.request.LoginRequest;
import com.effectivemobile.dto.request.RefreshTokenRequest;
import com.effectivemobile.dto.request.UserRequest;
import com.effectivemobile.dto.response.AuthResponse;
import com.effectivemobile.dto.response.MessageResponse;
import com.effectivemobile.dto.response.RefreshTokenResponse;
import com.effectivemobile.dto.response.UserResponse;
import com.effectivemobile.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final SecurityService securityService;

    @Operation(description = "Login user by email and password")
    @PostMapping("/signin")
    public AuthResponse authUser(@RequestBody @Valid LoginRequest loginRq) {
        return securityService.authenticate(loginRq);
    }

    @Operation(description = "Register new user")
    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody @Valid UserRequest createUserRq) {
        return securityService.register(createUserRq);
    }

    @Operation(description = "Refresh token")
    @PostMapping("/refresh-token")
    public RefreshTokenResponse refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRq) {
        return securityService.refreshToken(refreshTokenRq);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(description = "Logout user")
    @PostMapping("/logout")
    public MessageResponse logoutUser(@AuthenticationPrincipal UserDetails userDetails) {
        return securityService.logout();
    }
}
