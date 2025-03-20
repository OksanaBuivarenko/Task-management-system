package com.effectivemobile.security;

import com.effectivemobile.dto.request.LoginRequest;
import com.effectivemobile.dto.request.UserRequest;
import com.effectivemobile.dto.request.RefreshTokenRequest;
import com.effectivemobile.dto.response.AuthResponse;
import com.effectivemobile.dto.response.MessageResponse;
import com.effectivemobile.dto.response.RefreshTokenResponse;
import com.effectivemobile.dto.response.UserResponse;
import com.effectivemobile.entity.User;
import com.effectivemobile.entity.RefreshToken;
import com.effectivemobile.exception.RefreshTokenException;
import com.effectivemobile.security.jwt.JwtUtils;
import com.effectivemobile.service.UserService;
import com.effectivemobile.service.impl.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserService userService;

    public AuthResponse authenticate(LoginRequest loginRq) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRq.getEmail(),
                loginRq.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return AuthResponse.builder()
                .id(userDetails.getId())
                .email(userDetails.getUsername())
                .userName(userDetails.getName())
                .roles(roles)
                .refreshToken(refreshToken.getToken())
                .token(jwtUtils.generateJwtToken(userDetails))
                .build();
    }

    public UserResponse register(UserRequest createUserRq) {
        return userService.createUser(createUserRq);
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRq) {
        String requestRefreshToken = refreshTokenRq.getRefreshToken();

        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User tokenOwner = userService.getUserById(userId);
                    String token = jwtUtils.generateTokenFromUsername(tokenOwner.getUserName());
                    return new RefreshTokenResponse(token, refreshTokenService.createRefreshToken(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException(requestRefreshToken, "Refresh token not found"));
    }

    public MessageResponse logout() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            Long userId = userDetails.getId();
            refreshTokenService.deleteByUserId(userId);
        }
        return MessageResponse.builder().message("User logout!").build();
    }
}
