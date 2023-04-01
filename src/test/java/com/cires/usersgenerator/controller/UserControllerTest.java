package com.cires.usersgenerator.controller;

import com.cires.usersgenerator.common.JsonExporter;
import com.cires.usersgenerator.dto.AuthenticationRequest;
import com.cires.usersgenerator.dto.AuthenticationResponse;
import com.cires.usersgenerator.dto.UserDto;
import com.cires.usersgenerator.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    private JsonExporter jsonExporter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnBadRequestWhenCountParameterIsMissingInGenerateApi() throws Exception {
        Mockito.when(userService.generateUsersData(ArgumentMatchers.anyInt())).thenReturn(new byte[10]);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/generate")
                .content(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"description\" : \"Parameter count is required in the request\"")))
                .andReturn();
    }

    @Test
    void shouldReturnOkWhenCallingGenerateApi() throws Exception {
        UserDto user = UserDto.createRandomUserDto();
        String usersJsonString = jsonExporter.export(Collections.singletonList(user));
        Mockito.when(userService.generateUsersData(ArgumentMatchers.anyInt())).thenReturn(usersJsonString.getBytes());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/generate?count=1")
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
        Assertions.assertNotNull(result.getResponse().getContentAsByteArray());
        UserDto[] users = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto[].class);
        Optional<UserDto> userResult = Arrays.stream(users).findFirst();
        Assertions.assertTrue(userResult.isPresent());
    }

    @Test
    public void shouldReturnOkWhenCallingBatchApi() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "users_test.json", "multipart/form-data", "{\"json\": \"someValue\"}".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users/batch")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
    }

    @Test
    void shouldReturnOkWhenCallingAuthApi() throws Exception {
        Mockito.when(userService.authenticate(ArgumentMatchers.any())).thenReturn(new AuthenticationResponse("esdibsdibsdifbib"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                .content(objectMapper.writeValueAsString(new AuthenticationRequest("amine", "elaouni")))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
    }

    @Test
    void shouldThrowBadCredentialsExceptionWhenCallingAuthApi() throws Exception {
        Mockito.when(userService.authenticate(ArgumentMatchers.any())).thenThrow(new BadCredentialsException("Bad Credentials"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                .content(objectMapper.writeValueAsString(new AuthenticationRequest("amine", "elaouni")))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(500))
                .andReturn();
    }

    @Test
    void shouldThrowAuthenticationExceptionWhenCallingRetrieveAuthUserApi() throws Exception {
        Mockito.when(userService.retrieveAuthenticatedUser()).thenReturn(new UserDto());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", password = "pwd", authorities = { "admin" })
    void shouldReturnOkWhenCallingRetrieveAuthUserApi() throws Exception {
        Mockito.when(userService.retrieveAuthenticatedUser()).thenReturn(new UserDto());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                //.header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
    }
}
