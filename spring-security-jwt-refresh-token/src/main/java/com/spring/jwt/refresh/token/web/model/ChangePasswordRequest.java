package com.spring.jwt.refresh.token.web.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record ChangePasswordRequest(@NotBlank @Size(min = 8, max = 25) String password) {
}