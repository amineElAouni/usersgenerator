package com.cires.usersgenerator.controller;

import com.cires.usersgenerator.common.validators.FileValidator;
import com.cires.usersgenerator.dto.AuthenticationRequest;
import com.cires.usersgenerator.dto.AuthenticationResponse;
import com.cires.usersgenerator.dto.BatchResponse;
import com.cires.usersgenerator.dto.UserDto;
import com.cires.usersgenerator.exception.UserAccessDeniedProfileException;
import com.cires.usersgenerator.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;

import static com.cires.usersgenerator.exception.constant.ResponseMessageConstant.ACCESS_DENIED_USER_PROFILE_DESCRIPTION;
import static com.cires.usersgenerator.swagger.SwaggerConstant.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    private final MessageSource messageSource;

    @Operation(
            description = "Generate users in users.json file in download folder",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "File uploaded successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                    schema = @Schema(type = "file", format = "byte", description = "users.json"),
                                    examples = {
                                            @ExampleObject(
                                                    value = SUCCESSFULLY_RESPONSE_GENERATE_USERS_API
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request, Missing request parameter count in the request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = BAD_REQUEST_RESPONSE_GENERATE_USERS_API
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Interal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = INTERNAL_SERVER_ERROR_RESPONSE_ALL_API
                                            )
                                    }
                            )
                    )
            }
    )
    @GetMapping(value = "/users/generate")
    public ResponseEntity<byte[]> exportUsersToJsonFile(@RequestParam(value = "count") Integer count) {
        byte[] usersJsonBytes = userService.generateUsersData(count);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=users.json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(usersJsonBytes.length)
                .body(usersJsonBytes);
    }

    @Operation(
            description = "Save users contained in the users.json file in the database ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users saved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = SUCCESSFULLY_RESPONSE_SAVE_USERS_API
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request, Missing required parameter json file in the request",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = BAD_REQUEST_RESPONSE_SAVE_USERS_API
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Interal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = INTERNAL_SERVER_ERROR_RESPONSE_ALL_API
                                            )
                                    }
                            )
                    )
            }
    )
    @PostMapping(value = "/users/batch", consumes = { "multipart/form-data" })
    public ResponseEntity<BatchResponse> saveUsersToDatabase(@RequestParam("file") @FileValidator MultipartFile file) throws IOException {
        BatchResponse batchResponse = userService.saveUsers(file);
        return ResponseEntity
                .ok()
                .body(batchResponse);
    }

    @Operation(
            description = "User Authentication",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User is successfully authenticated",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = SUCCESSFULLY_RESPONSE_AUTH_API
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request, Field must not be blank",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = BAD_REQUEST_RESPONSE_AUTH_API
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized, Failed Authentication",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = UNAUTHORIZED_RESPONSE_AUTH_API
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Interal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = INTERNAL_SERVER_ERROR_RESPONSE_ALL_API
                                            )
                                    }
                            )
                    )
            }
    )
    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.authenticate(authenticationRequest));
    }

    @Operation(
            description = "Retrieve Authenticated User",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User is successfully retrieved",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = SUCCESSFULLY_RESPONSE_RETRIEVE_USER_API
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized, Failed Authentication",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = UNAUTHORIZED_JWT_TOKEN_EXPIRED_RESPONSE
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Interal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = INTERNAL_SERVER_ERROR_RESPONSE_ALL_API
                                            )
                                    }
                            )
                    )
            }
    )
    @SecurityRequirement(name = BEARER_AUTHENTICATION)
    @GetMapping("/users/me")
    public ResponseEntity<UserDto> retrieveAuthenticatedUser() {
        return ResponseEntity.ok(userService.retrieveAuthenticatedUser());
    }

    @Operation(
            description = "Retrieve User by Username",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User is successfully retrieved",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = SUCCESSFULLY_RESPONSE_RETRIEVE_USER_API
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized, Failed Authentication",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = UNAUTHORIZED_JWT_TOKEN_EXPIRED_RESPONSE
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden, Unable to access user profile",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = FORBIDDEN_ACCES_USER_PROFILE_RESPONSE
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found, User not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = USER_NOT_FOUND_RESPONSE
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Interal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = INTERNAL_SERVER_ERROR_RESPONSE_ALL_API
                                            )
                                    }
                            )
                    )
            }
    )
    @SecurityRequirement(name = BEARER_AUTHENTICATION)
    @GetMapping("/users/{username}")
    public ResponseEntity<UserDto> retrieveUser(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.retrieveUser(username));
        } catch (AccessDeniedException ex) {
            throw new UserAccessDeniedProfileException(
                    messageSource.getMessage(ACCESS_DENIED_USER_PROFILE_DESCRIPTION,
                            new String[]{username},
                            Locale.ENGLISH));
        }
    }
}
