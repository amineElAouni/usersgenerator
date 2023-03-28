package com.cires.usersgenerator.exception.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseMessageConstant {

    public static final String RESOURCE_NOT_FOUND_MESSAGE = "exceptionResponse.notFound.message";

    public static final String USER_NOT_FOUND_DESCRIPTION = "user.notFound.description";

    public static final String JWT_SIGNATURE_EXCEPTION_DESCRIPTION = "jwt.signature.exception.message";

    public static final String JWT_MALFORMED_EXCEPTION_DESCRIPTION = "jwt.malformed.exception.message";

    public static final String JWT_EXPIRED_EXCEPTION_DESCRIPTION = "jwt.expired.exception.message";

    public static final String JWT_UNSUPPORTED_EXCEPTION_DESCRIPTION = "jwt.unsupported.exception.message";

    public static final String JWT_ILLEGAL_ARGUMENT_EXCEPTION_DESCRIPTION = "jwt.illegal.argument.exception.message";

    public static final String AUTHENTICATION_ERROR_MESSAGE = "authentication.error.message";

    public static final String AUTHENTICATION_ERROR_DESCRIPTION = "authentication.error.description";

    public static final String ACCESS_DENIED_USER_PROFILE_DESCRIPTION = "access.denied.user.profile.description";

    public static final String ACCESS_DENIED_USER_PROFILE_MESSAGE = "access.denied.user.profile.message";

    public static final String MISSING_REQUEST_PARAMETER_API_MESSAGE = "missing.request.parameter.api.message";

    public static final String MISSING_REQUEST_PARAMETER_API_DESCRIPTION = "missing.request.parameter.api.description";

    public static final String MISSING_JSON_FILE_MESSAGE = "missing.json.file.message";

    public static final String MISSING_JSON_FILE_DESCRIPTION = "missing.json.file.description";

    public static final String INVALID_FIELDS_MESSAGE = "exceptionResponse.invalidFields.message";

    public static final String INVALID_FIELDS_NOT_BLANK_DESCRIPTION = "exceptionResponse.invalidFields.notBlank.description";

    public static final String INTERNAL_TECHNICAL_ERROR_MESSAGE = "exceptionResponse.technical.error.message";
}
