package com.cires.usersgenerator.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneratorConstant {

    public static final String UPPER_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String LOWER_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";

    public static final String NUMBERS = "0123456789";

    public static final String SPECIAL_CHARACTERS = "<>.,?/{}[]+_-)(*&%$@!=";

    public static final String ROLE_USER = "user";

    public static final String ROLE_ADMIN = "admin";

    public static final int MIN_BOUND = 6;

    public static final int MAX_BOUND = 10;
}
