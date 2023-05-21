package com.spring.jwt.refresh.token.web;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.spring.jwt.refresh.token.common.TokenException;
import com.spring.jwt.refresh.token.persistance.refresh.RefreshTokenEntity;
import com.spring.jwt.refresh.token.persistance.user.UserEntity;
import com.spring.jwt.refresh.token.security.TokenProvider;
import com.spring.jwt.refresh.token.service.RefreshTokenService;
import com.spring.jwt.refresh.token.service.UserService;
import com.spring.jwt.refresh.token.web.model.RefreshTokenRequest;
import com.spring.jwt.refresh.token.web.model.RefreshTokenResponse;
import com.spring.jwt.refresh.token.web.model.TokenResponse;
import com.spring.jwt.refresh.token.web.model.UsernameAndPasswordRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/jwt")
@AllArgsConstructor
public class JwtController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping(
            path = "token",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TokenResponse> authorize(@Valid @RequestBody UsernameAndPasswordRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        var jwt = tokenProvider.generate(authentication.getName(), authentication.getAuthorities());
        var refreshToken = refreshTokenService.create(authentication.getName());
        var authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new TokenResponse(authentication.getName(), authorities, refreshToken, jwt));
    }

    @PostMapping(
            path = "refresh",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        var refreshToken = request.refreshToken();
        var principal = refreshTokenService.findById(refreshToken)
                .filter(refreshTokenService::isExpired)
                .map(RefreshTokenEntity::getUsername)
                .map(userService::loadUserByUsername)
                .orElseThrow(() -> new TokenException("Refresh token do not exists"));
        var jwt = tokenProvider.generate(principal.getUsername(), principal.getAuthorities());
        return ResponseEntity.ok(new RefreshTokenResponse(refreshToken, jwt));
    }

    @GetMapping("me")
    public ResponseEntity<UserEntity> me() {
        var username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok(userService.findById(username));
    }
}
