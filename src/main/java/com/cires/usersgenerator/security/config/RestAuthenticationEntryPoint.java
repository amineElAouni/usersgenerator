package com.cires.usersgenerator.security.config;

import com.cires.usersgenerator.exception.dto.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Locale;

import static com.cires.usersgenerator.exception.constant.ResponseMessageConstant.AUTHENTICATION_ERROR_MESSAGE;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    private final MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authException) throws IOException {
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                messageSource.getMessage(AUTHENTICATION_ERROR_MESSAGE, null, Locale.ENGLISH),
                authException.getMessage(),
                unauthorized,
                LocalDateTime.now());
        OutputStream out = httpServletResponse.getOutputStream();
        httpServletResponse.setStatus(unauthorized.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, apiErrorResponse);
        out.flush();
    }
}
