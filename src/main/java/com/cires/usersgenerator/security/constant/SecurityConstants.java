package com.cires.usersgenerator.security.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

    public static final String SECRET_KEY = "78214125442A472D4A614E645267556B58703273357638792F423F4528482B4D";

    public static final long TOKEN_EXPIRATION_TIME = 86400000;

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String AUTHORITIES= "roles";

}
