package com.spring.jwt.refresh.token.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.spring.jwt.refresh.token.common.TokenException;
import com.spring.jwt.refresh.token.config.JwtProperties;
import com.spring.jwt.refresh.token.persistance.refresh.RefreshTokenEntity;
import com.spring.jwt.refresh.token.persistance.refresh.RefreshTokenRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final JwtProperties properties;

    public String create(String username) {
        var r = new RefreshTokenEntity();
        r.setUsername(username);
        r.setRefreshToken(UUID.randomUUID().toString());
        r.setExpiration(Instant.now().plus(properties.getRefreshExpiration()));
        return repository.save(r).getRefreshToken();
    }

    public Optional<RefreshTokenEntity> findById(String refreshToken) {
        return repository.findById(refreshToken);
    }

    public boolean isExpired(RefreshTokenEntity entity) {
        if (entity.getExpiration().isBefore(Instant.now())) {
            throw new TokenException("Refresh token is expired");
        }
        return true;
    }
}
