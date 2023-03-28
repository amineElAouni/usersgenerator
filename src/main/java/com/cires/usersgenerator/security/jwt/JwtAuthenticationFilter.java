package com.cires.usersgenerator.security.jwt;

import com.cires.usersgenerator.exception.dto.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

import static com.cires.usersgenerator.exception.constant.ResponseMessageConstant.*;
import static com.cires.usersgenerator.security.constant.SecurityConstants.AUTHORIZATION_HEADER;
import static com.cires.usersgenerator.security.constant.SecurityConstants.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper;

    private final MessageSource messageSource;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
            final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
            final String jwt;
            final String userEmail;
            if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
            try {
                jwt = authHeader.substring(7);
                userEmail = jwtService.extractUserName(jwt);
                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
                filterChain.doFilter(request, response);
            } catch (SignatureException ex){
                buildJwtExceptionErrorMessage(JWT_SIGNATURE_EXCEPTION_DESCRIPTION, response);
            }
            catch (MalformedJwtException ex){
                buildJwtExceptionErrorMessage(JWT_MALFORMED_EXCEPTION_DESCRIPTION, response);
            }
            catch (ExpiredJwtException ex){
                buildJwtExceptionErrorMessage(JWT_EXPIRED_EXCEPTION_DESCRIPTION, response);
            }
            catch (UnsupportedJwtException ex){
                buildJwtExceptionErrorMessage(JWT_UNSUPPORTED_EXCEPTION_DESCRIPTION, response);
            }
            catch (IllegalArgumentException ex){
                buildJwtExceptionErrorMessage(JWT_ILLEGAL_ARGUMENT_EXCEPTION_DESCRIPTION, response);
            }
    }

    private void buildJwtExceptionErrorMessage(String description, HttpServletResponse response) throws IOException{
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                messageSource.getMessage(AUTHENTICATION_ERROR_MESSAGE, null, Locale.ENGLISH),
                messageSource.getMessage(description, null, Locale.ENGLISH),
                unauthorized,
                LocalDateTime.now());
        response.setStatus(unauthorized.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiErrorResponse));
    }
}
