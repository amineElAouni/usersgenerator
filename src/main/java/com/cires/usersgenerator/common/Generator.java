package com.cires.usersgenerator.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.cires.usersgenerator.common.GeneratorConstant.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Generator {

    public static String generateRandomRole() {
        try {
            List<String> roles = Arrays.asList(ROLE, ADMIN);
            Random random = SecureRandom.getInstanceStrong();
            return roles.get(random.nextInt(roles.size()));
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        return "";
    }

    public static String generateRandomPassword() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            String combination = UPPER_CHARACTERS + LOWER_CHARACTERS + SPECIAL_CHARACTERS + NUMBERS;
            int length = random.nextInt(MAX_BOUND - MIN_BOUND) + MIN_BOUND;
            char[] password = new char[length];
            for (int i = 0; i < length; i++) {
                password[i] = combination.charAt(random.nextInt(combination.length()));
            }
            return String.valueOf(password);
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        return "";
    }
}
