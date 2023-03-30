package com.cires.usersgenerator.swagger;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SwaggerConstant {

    public static final String BEARER_AUTHENTICATION = "Bearer Authentication";

    public static final String JWT = "JWT";

    public static final String BEARER = "bearer";

    public static final String SUCCESSFULLY_RESPONSE_GENERATE_USERS_API = """
                                                            [{
                                                                "firstName": "Sherise",
                                                                "lastName": "Shields",
                                                                "birthDate": "1987-07-26",
                                                                "city": "Valberg",
                                                                "country": "hu",
                                                                "avatar": "https://robohash.org/ptiswmtu.png",
                                                                "company": "Leffler Inc",
                                                                "jobPosition": "Engineer",
                                                                "mobile": "412-848-4810",
                                                                "userName": "maynard.hoppe",
                                                                "email": "arron.connelly@hotmail.com",
                                                                "password": "JE5/x%!y&",
                                                                "role": "admin"
                                                              }]""";

    public static final String BAD_REQUEST_RESPONSE_GENERATE_USERS_API = """
                                                            {
                                                                "message": "Missing Request Parameter API",
                                                                "description": "Parameter count is required in the request",
                                                                "httpStatus": "BAD_REQUEST",
                                                                "timestamp": "29-03-2023 10:38:41"
                                                            }""";

    public static final String INTERNAL_SERVER_ERROR_RESPONSE_ALL_API = """
                                                            {
                                                                "message": "Technical Error",
                                                                "description": "Technical Error has occured",
                                                                "httpStatus": "INTERNAL_SERVER_ERROR",
                                                                "timestamp": "30-03-2023 01:24:24"
                                                            }""";

    public static final String SUCCESSFULLY_RESPONSE_SAVE_USERS_API = """
                                                            {
                                                              "usersImportedWithSuccessCount": 10,
                                                              "usersNotImportedWithSuccessCount": 0
                                                            }""";

    public static final String BAD_REQUEST_RESPONSE_SAVE_USERS_API = """
                                                            {
                                                                "message": "File Not Found",
                                                                "description": "Missing required parameter json file in the request",
                                                                "httpStatus": "BAD_REQUEST",
                                                                "timestamp": "30-03-2023 01:52:51"
                                                            }""";

    public static final String SUCCESSFULLY_RESPONSE_AUTH_API = """
                                                            {
                                                              "accessToken": "string"
                                                            }""";

    public static final String BAD_REQUEST_RESPONSE_AUTH_API = """
                                                            {
                                                                "message": "Invalid Body Field",
                                                                "description": "Field username must not be blank",
                                                                "httpStatus": "BAD_REQUEST",
                                                                "timestamp": "30-03-2023 02:22:05"
                                                            }""";

    public static final String UNAUTHORIZED_RESPONSE_AUTH_API = """
                                                            {
                                                                "message": "Failed Authentication",
                                                                "description": "Username and password are incorrect",
                                                                "httpStatus": "UNAUTHORIZED",
                                                                "timestamp": "30-03-2023 02:21:07"
                                                            }""";

    public static final String SUCCESSFULLY_RESPONSE_RETRIEVE_USER_API = """
                                                            {
                                                                "firstName": "Sherise",
                                                                "lastName": "Shields",
                                                                "birthDate": "1987-07-26",
                                                                "city": "Valberg",
                                                                "country": "hu",
                                                                "avatar": "https://robohash.org/ptiswmtu.png",
                                                                "company": "Leffler Inc",
                                                                "jobPosition": "Engineer",
                                                                "mobile": "412-848-4810",
                                                                "userName": "maynard.hoppe",
                                                                "email": "arron.connelly@hotmail.com",
                                                                "password": "JE5/x%!y&",
                                                                "role": "admin"
                                                              }""";

    public static final String UNAUTHORIZED_JWT_TOKEN_EXPIRED_RESPONSE = """
                                                            {
                                                                "message": "Failed Authentication",
                                                                "description": "Expired JWT token",
                                                                "httpStatus": "UNAUTHORIZED",
                                                                "timestamp": "30-03-2023 03:10:56"
                                                            }""";

    public static final String USER_NOT_FOUND_RESPONSE = """
                                                            {
                                                                "message": "Resource Not Found",
                                                                "description": "User with username test not found in the database",
                                                                "httpStatus": "NOT_FOUND",
                                                                "timestamp": "30-03-2023 03:18:44"
                                                            }""";

    public static final String FORBIDDEN_ACCES_USER_PROFILE_RESPONSE = """
                                                            {
                                                                "message": "Profile Access Denied",
                                                                "description": "Unable to access user profile",
                                                                "httpStatus": "FORBIDDEN",
                                                                "timestamp": "30-03-2023 03:16:34"
                                                            }""";

}
