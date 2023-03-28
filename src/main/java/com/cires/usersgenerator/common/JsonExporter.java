package com.cires.usersgenerator.common;

import com.cires.usersgenerator.dto.UserDto;

import java.util.List;

public interface JsonExporter {

    String export(List<UserDto> users);
}
