package com.spring.jwt.refresh.token.model;

import java.util.List;

public record TokenResponse(String username, List<String> authorities, String refreshToken, String jwt) {
}
