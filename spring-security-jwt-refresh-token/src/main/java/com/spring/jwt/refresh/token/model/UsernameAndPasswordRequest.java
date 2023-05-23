package com.spring.jwt.refresh.token.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UsernameAndPasswordRequest(@NotBlank @Size(min = 2, max = 25) String username,
                                         @NotBlank @Size(min = 8, max = 25) String password) {
}
