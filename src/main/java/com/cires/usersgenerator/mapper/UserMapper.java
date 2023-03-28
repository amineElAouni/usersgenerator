package com.cires.usersgenerator.mapper;

import com.cires.usersgenerator.dto.UserDto;
import com.cires.usersgenerator.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User mapUserDtoToUser(UserDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .avatar(userDto.getAvatar())
                .birthDate(userDto.getBirthDate())
                .city(userDto.getCity())
                .company(userDto.getCompany())
                .country(userDto.getCountry())
                .email(userDto.getEmail())
                .jobPosition(userDto.getJobPosition())
                .mobile(userDto.getMobile())
                .role(userDto.getRole())
                .userName(userDto.getUserName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
    }

    public UserDto mapUserToUserDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatar(user.getAvatar())
                .birthDate(user.getBirthDate())
                .city(user.getCity())
                .company(user.getCompany())
                .country(user.getCountry())
                .email(user.getEmail())
                .jobPosition(user.getJobPosition())
                .mobile(user.getMobile())
                .role(user.getRole())
                .userName(user.getUserName())
                .build();
    }
}
