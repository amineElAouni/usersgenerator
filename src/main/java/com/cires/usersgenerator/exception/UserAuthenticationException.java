package com.cires.usersgenerator.exception;

import java.io.Serial;

public class UserAuthenticationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2477682054344917471L;

    public UserAuthenticationException(String msg) {
        super(msg);
    }
}
