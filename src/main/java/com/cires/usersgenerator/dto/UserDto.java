package com.cires.usersgenerator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.ZoneId;

import static com.cires.usersgenerator.common.Generator.generateRandomPassword;
import static com.cires.usersgenerator.common.Generator.generateRandomRole;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String firstName;

    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String city;

    private String country;

    private String avatar;

    private String company;

    private String jobPosition;

    private String mobile;

    private String userName;

    private String email;

    private String password;

    private String role;

    public static UserDto createRandomUserDto() {
        return UserDto.builder()
                .firstName(Faker.instance().name().firstName())
                .lastName(Faker.instance().name().lastName())
                .birthDate(Faker.instance().date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .city(Faker.instance().address().city())
                .avatar(Faker.instance().avatar().image())
                .company(Faker.instance().company().name())
                .country(Faker.instance().country().countryCode2())
                .email(Faker.instance().internet().emailAddress())
                .jobPosition(Faker.instance().job().position())
                .mobile(Faker.instance().phoneNumber().cellPhone())
                .userName(Faker.instance().name().username())
                .password(generateRandomPassword())
                .role(generateRandomRole())
                .build();
    }
}
