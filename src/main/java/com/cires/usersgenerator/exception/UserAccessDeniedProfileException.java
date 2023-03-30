package com.cires.usersgenerator.exception;

import java.io.Serial;

public class UserAccessDeniedProfileException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -4925890648451907363L;

    public UserAccessDeniedProfileException(String msg) {
        super(msg);
    }
}
