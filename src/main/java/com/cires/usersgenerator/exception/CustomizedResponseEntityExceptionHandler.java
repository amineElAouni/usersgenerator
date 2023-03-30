package com.cires.usersgenerator.exception;

import com.cires.usersgenerator.exception.dto.ApiErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

import static com.cires.usersgenerator.exception.constant.ErrorInstanceNameConstant.FILE_VALIDATOR;
import static com.cires.usersgenerator.exception.constant.ErrorInstanceNameConstant.NOT_BLANK;
import static com.cires.usersgenerator.exception.constant.ResponseMessageConstant.*;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                messageSource.getMessage(RESOURCE_NOT_FOUND_MESSAGE, null, Locale.ENGLISH),
                ex.getMessage(),
                notFound,
                LocalDateTime.now());
        return new ResponseEntity<>(apiErrorResponse, notFound);
    }

    @ExceptionHandler(UserAccessDeniedProfileException.class)
    public final ResponseEntity<Object> handleUserAccessDeniedProfileException(UserAccessDeniedProfileException ex) {
        HttpStatus forbidden = HttpStatus.FORBIDDEN;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                messageSource.getMessage(ACCESS_DENIED_USER_PROFILE_MESSAGE, null, Locale.ENGLISH),
                ex.getMessage(),
                forbidden,
                LocalDateTime.now());
        return new ResponseEntity<>(apiErrorResponse, forbidden);
    }

    @ExceptionHandler(UserAuthenticationException.class)
    public final ResponseEntity<Object> handleUserAuthenticationException(UserAuthenticationException ex) {
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                messageSource.getMessage(AUTHENTICATION_ERROR_MESSAGE, null, Locale.ENGLISH),
                ex.getMessage(),
                unauthorized,
                LocalDateTime.now());
        return new ResponseEntity<>(apiErrorResponse, unauthorized);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiErrorResponse apiErrorResponse = null;
        ConstraintViolation<?> violation = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .get();
        if (violation.getConstraintDescriptor() != null
                && violation.getConstraintDescriptor().getAnnotation() != null
                && violation.getConstraintDescriptor().getAnnotation().annotationType().getName().endsWith(FILE_VALIDATOR)) {
                    apiErrorResponse = new ApiErrorResponse(
                    messageSource.getMessage(MISSING_JSON_FILE_MESSAGE, null, Locale.ENGLISH),
                    violation.getMessage(),
                    badRequest,
                    LocalDateTime.now());
        }
        return new ResponseEntity<>(apiErrorResponse, badRequest);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        HttpStatus serverError = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                messageSource.getMessage(INTERNAL_TECHNICAL_ERROR_MESSAGE, null, Locale.ENGLISH),
                ex.getMessage(),
                serverError,
                LocalDateTime.now());
        return new ResponseEntity<>(apiErrorResponse, serverError);
    }

    @Override
    protected final ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                messageSource.getMessage(MISSING_REQUEST_PARAMETER_API_MESSAGE, null, Locale.ENGLISH),
                messageSource.getMessage(MISSING_REQUEST_PARAMETER_API_DESCRIPTION, new String[] {ex.getParameterName()}, Locale.ENGLISH),
                badRequest,
                LocalDateTime.now());
        return new ResponseEntity<>(apiErrorResponse, badRequest);
    }

    @Override
    protected final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiErrorResponse apiErrorResponse;
        FieldError fieldError = Objects.requireNonNull(ex.getFieldError());
        String exceptionCode = Objects.requireNonNull(ex.getBindingResult().getFieldError().getCode());
        if (NOT_BLANK.equals(exceptionCode)) {
            String[] args = new String[2];
            args[0] = fieldError.getField();
            args[1] = fieldError.getDefaultMessage();
            apiErrorResponse = new ApiErrorResponse(
                    messageSource.getMessage(INVALID_FIELDS_MESSAGE, null, Locale.ENGLISH),
                    messageSource.getMessage(INVALID_FIELDS_NOT_BLANK_DESCRIPTION, args, Locale.ENGLISH),
                    badRequest,
                    LocalDateTime.now());
        }
        else {
            apiErrorResponse = new ApiErrorResponse(
                    messageSource.getMessage(INVALID_FIELDS_MESSAGE, null, Locale.ENGLISH),
                    ex.getMessage(),
                    badRequest,
                    LocalDateTime.now());
        }
        return new ResponseEntity<>(apiErrorResponse, badRequest);
    }

    @Override
    protected final ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                    messageSource.getMessage(INVALID_FIELDS_MESSAGE, null, Locale.ENGLISH),
                    ex.getMessage(),
                    badRequest,
                    LocalDateTime.now());
        return new ResponseEntity<>(apiErrorResponse, badRequest);
    }
}
