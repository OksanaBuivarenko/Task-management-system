package com.effectivemobile.service.impl;

import com.effectivemobile.entity.RefreshToken;
import com.effectivemobile.exception.RefreshTokenException;
import com.effectivemobile.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void findByRefreshToken() {
        String token = "testToken";
        RefreshToken refreshToken = RefreshToken.builder().token(token).build();

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> result = refreshTokenService.findByRefreshToken(token);

        assertTrue(result.isPresent());
        assertEquals(token, result.get().getToken());
    }

    @Test
    void checkRefreshTokenExpired() {
        RefreshToken token = RefreshToken.builder()
                .token("expiredToken")
                .expiredDate(Instant.now().minusSeconds(1))
                .build();

        assertThrows(RefreshTokenException.class, () -> refreshTokenService.checkRefreshToken(token));
    }

    @Test
    void checkRefreshTokenValid() {
        RefreshToken token = RefreshToken.builder()
                .token("expiredToken")
                .expiredDate(Instant.now().plusSeconds(60))
                .build();

        RefreshToken result = refreshTokenService.checkRefreshToken(token);

        assertEquals(token, result);
    }

    @Test
    void deleteByUserId() {
        refreshTokenService.deleteByUserId(1L);
        verify(refreshTokenRepository, times(1)).deleteByUserId(1L);
    }
}