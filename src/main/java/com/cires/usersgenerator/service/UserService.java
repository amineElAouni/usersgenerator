package com.cires.usersgenerator.service;

import com.cires.usersgenerator.common.JsonExporter;
import com.cires.usersgenerator.dto.AuthenticationRequest;
import com.cires.usersgenerator.dto.AuthenticationResponse;
import com.cires.usersgenerator.dto.BatchResponse;
import com.cires.usersgenerator.dto.UserDto;
import com.cires.usersgenerator.exception.UserAccessDeniedProfile;
import com.cires.usersgenerator.mapper.UserMapper;
import com.cires.usersgenerator.model.User;
import com.cires.usersgenerator.repository.UserRepository;
import com.cires.usersgenerator.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.cires.usersgenerator.common.GeneratorConstant.ADMIN;
import static com.cires.usersgenerator.dto.UserDto.createRandomUserDto;
import static com.cires.usersgenerator.exception.constant.ResponseMessageConstant.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;

    private final JsonExporter jsonExporter;

    private final ObjectMapper objectMapper;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final MessageSource messageSource;

    public byte[] generateUsersData(Integer count) {
        List<UserDto> users = new ArrayList<>();
        IntStream.range(0, count)
                .forEach(i -> users.add(createRandomUserDto()));
        String usersJsonString = jsonExporter.export(users);
        return usersJsonString.getBytes();
    }

    public BatchResponse saveUsers(MultipartFile file) throws IOException {
        UserDto[] userDtos = objectMapper.readValue(file.getBytes(), UserDto[].class);
        List<User> users = Arrays.stream(userDtos)
                                 .map(userMapper::mapUserDtoToUser)
                                 .filter(distinctByKey(User::getUsername))
                                 .filter(distinctByKey(User::getEmail))
                                 .toList();
        BatchResponse batchResponse = BatchResponse.builder()
                                  .usersImportedWithSuccessCount(users.size())
                                  .usersNotImportedWithSuccessCount(userDtos.length - users.size())
                                  .build();
        userRepository.saveAll(users);
        return batchResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        String token = null;
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));
        Optional<User> user = userRepository.findByEmailOrUsername(authenticationRequest.getUsername());
        if (user.isPresent()) {
            token = jwtService.generateToken(user.get());
        }
        return AuthenticationResponse.builder()
                .jwtToken(token)
                .build();
    }

    public UserDto retrieveAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserDto> userDto = userRepository.findByEmail(((User) authentication.getPrincipal()).getEmail())
                .map(userMapper::mapUserToUserDto);
        return userDto.orElseThrow(() -> new UsernameNotFoundException(
                        messageSource.getMessage(USER_NOT_FOUND_DESCRIPTION,
                        new String[] {((User) authentication.getPrincipal()).getUserName()},
                        Locale.ENGLISH)));
    }

    public UserDto retrieveUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (getAuthorities(authentication.getAuthorities()).equals(ADMIN)) {
            Optional<UserDto> userDto = userRepository.findByEmailOrUsername(username)
                    .map(userMapper::mapUserToUserDto);
            return userDto.orElseThrow(() -> new UsernameNotFoundException(
                            messageSource.getMessage(USER_NOT_FOUND_DESCRIPTION,
                            new String[] {username},
                            Locale.ENGLISH)));
        }
        else {
            throw new UserAccessDeniedProfile(
                    messageSource.getMessage(ACCESS_DENIED_USER_PROFILE_DESCRIPTION,
                            new String[]{username},
                            Locale.ENGLISH));
        }
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private String getAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
