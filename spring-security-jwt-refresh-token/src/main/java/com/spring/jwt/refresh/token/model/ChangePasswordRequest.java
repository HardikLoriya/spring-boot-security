package com.spring.jwt.refresh.token.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record ChangePasswordRequest(@NotBlank @Size(min = 8, max = 25, message = "length should be 8 to 25.") String password) {
}