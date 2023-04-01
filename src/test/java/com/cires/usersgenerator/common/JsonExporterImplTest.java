package com.cires.usersgenerator.common;

import com.cires.usersgenerator.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@SpringBootTest
public class JsonExporterImplTest {

    @Autowired
    private JsonExporter jsonExporter;

    @Test
    public void test() {
        UserDto user = UserDto.createRandomUserDto();
        String usersJsonString = jsonExporter.export(Collections.singletonList(user));
        Assertions.assertTrue(!usersJsonString.isBlank());
    }
}
