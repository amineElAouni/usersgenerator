package com.cires.usersgenerator.mapper;

import com.cires.usersgenerator.domain.User;
import com.cires.usersgenerator.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User mapUserDtoToUser(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    UserDto mapUserToUserDto(User user);
}
