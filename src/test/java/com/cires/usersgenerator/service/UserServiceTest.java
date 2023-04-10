package com.cires.usersgenerator.service;

import com.cires.usersgenerator.domain.User;
import com.cires.usersgenerator.dto.AuthenticationRequest;
import com.cires.usersgenerator.dto.AuthenticationResponse;
import com.cires.usersgenerator.dto.BatchResponse;
import com.cires.usersgenerator.dto.UserDto;
import com.cires.usersgenerator.exception.UserAuthenticationException;
import com.cires.usersgenerator.mapper.UserMapper;
import com.cires.usersgenerator.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceLoader resourceLoader;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
     void shouldGenerateUsersData() {
        byte[] usersJsonBytes = userService.generateUsersData(2);
        Assertions.assertNotNull(usersJsonBytes);
    }

    @Test
     void shouldSaveUsersInDatabase() throws IOException {
        final Resource fileResource = resourceLoader.getResource("classpath:users_test.json");
        InputStream inputStream = fileResource.getInputStream();
        MultipartFile result = new MockMultipartFile("file", "users_test.json", "multipart/form-data", inputStream);
        User[] users = objectMapper.readValue(result.getInputStream().readAllBytes(), User[].class);
        Mockito.when(userRepository.saveAll(ArgumentMatchers.any())).thenReturn(Arrays.asList(users));
        BatchResponse batchResponse = userService.saveUsers(result);
        Assertions.assertEquals(batchResponse.getUsersImportedWithSuccessCount(), 1);
        Assertions.assertEquals(batchResponse.getUsersNotImportedWithSuccessCount(), 0);
    }

    @Test
    void shouldThrowUserAuthenticationExceptionInAuthApi() {
        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any())).thenThrow(new UserAuthenticationException("Username and password are incorrect"));
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("Stephane")
                .password("Abc1234")
                .build();
        Assertions.assertThrows(UserAuthenticationException.class, () -> userService.authenticate(request));
    }

    @Test
    void shouldGenerateTokenWhenCallAuthApi() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("Stephane")
                .password("Abc1234")
                .build();
        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any())).thenReturn(ArgumentMatchers.any());
        Mockito.when(userRepository.findByEmailOrUsername(request.getUsername())).thenReturn(Optional.of(UserDto.createRandomUserDto()).map(userMapper::mapUserDtoToUser));
        AuthenticationResponse authenticationResponse = userService.authenticate(request);
        Assertions.assertNotNull(authenticationResponse.getAccessToken());
    }

    @Test
    public void shouldAuthenticateUserWhenCallRetrieveAuthenticatedUserApi() {
        Optional<User> optionalUser = Optional.of(UserDto.createRandomUserDto()).map(userMapper::mapUserDtoToUser);
        optionalUser.ifPresent(user -> {
            Mockito.when(userDetailsService.loadUserByUsername(ArgumentMatchers.any())).thenReturn(user);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority(user.getRole())));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        });
        Mockito.when(userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(optionalUser);
        UserDto userDto = userService.retrieveAuthenticatedUser();
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userDto.getUserName(), optionalUser.get().getUserName());

    }

    @Test
    @WithMockUser(username = "Test", password = "pwd", authorities = { "admin" })
    public void shouldThrowUsernameNotFoundExceptionInRetrieveUserByUsernameApi() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.retrieveUser("Test"));
    }

    @Test
    @WithMockUser(username = "Test", password = "pwd", authorities = { "user" })
    public void shouldThrowAccessDeniedExceptionInRetrieveUserByUsernameApi() {
        Assertions.assertThrows(AccessDeniedException.class, () -> userService.retrieveUser("Test"));
    }

    @Test
    @WithMockUser(username = "Test", password = "pwd", authorities = { "admin" })
    public void shouldTestInRetrieveUserByUsernameApi() {
        Optional<User> optionalUser = Optional.of(UserDto.createRandomUserDto()).map(userMapper::mapUserDtoToUser);
        Mockito.when(userRepository.findByEmailOrUsername(ArgumentMatchers.any())).thenReturn(optionalUser);
        UserDto userDto = userService.retrieveUser("Test");
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userDto.getUserName(), optionalUser.get().getUserName());
    }

}
