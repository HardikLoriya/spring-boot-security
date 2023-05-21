package com.spring.jwt.refresh.token.common;

import org.springframework.security.core.AuthenticationException;

public class TokenException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

	public TokenException(String msg) {
        super(msg);
    }

    public TokenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
