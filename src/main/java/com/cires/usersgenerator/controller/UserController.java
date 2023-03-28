package com.cires.usersgenerator.controller;

import com.cires.usersgenerator.common.validators.FileValidator;
import com.cires.usersgenerator.dto.AuthenticationRequest;
import com.cires.usersgenerator.dto.AuthenticationResponse;
import com.cires.usersgenerator.dto.BatchResponse;
import com.cires.usersgenerator.dto.UserDto;
import com.cires.usersgenerator.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/users/generate")
    public ResponseEntity<byte[]> exportUsersToJsonFile(@RequestParam(value = "count") Integer count) {
        byte[] usersJsonBytes = userService.generateUsersData(count);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=users.json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(usersJsonBytes.length)
                .body(usersJsonBytes);
    }

    @PostMapping("/users/batch")
    public ResponseEntity<BatchResponse> saveUsersToDatabase(@RequestParam("file") @FileValidator MultipartFile file) throws IOException {
        BatchResponse batchResponse = userService.saveUsers(file);
        return ResponseEntity
                .ok()
                .body(batchResponse);
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.authenticate(authenticationRequest));
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserDto> retrieveAuthenticatedUser() {
        return ResponseEntity.ok(userService.retrieveAuthenticatedUser());
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDto> retrieveUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.retrieveUser(username));
    }
}
