package com.shongon.identity_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shongon.identity_service.dto.request.user.CreateUserRequest;
import com.shongon.identity_service.dto.response.user.CreateUserResponse;
import com.shongon.identity_service.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private CreateUserRequest createUserRequest;
    private CreateUserResponse createUserResponse;

    @BeforeEach
    void initData() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        // sample input
        createUserRequest = CreateUserRequest.builder()
                .username("test")
                .firstName("test")
                .lastName("test")
                .password("12345678")
                .birthDate(birthDate)
                .build();
        // sample output
        createUserResponse =
                CreateUserResponse.builder().message("Create user success!").build();
    }

    @Test
    void register_validRequest_success() throws Exception {
        // Given
        // Sử dụng ObjectMapper để chuyển nội dung trong "createUserRequest" thành String
        ObjectMapper objectMapper = new ObjectMapper();
        // Thông báo cho Json khi add thư viện serialize LocalDate to Json
        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(createUserRequest);

        // Thay vì gọi xuống method ở userService thì trả về trực tiếp response mẫu
        when(userService.createUser(any())).thenReturn(createUserResponse);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register") // request method in postman
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                // Then
                // HttpStatusCode = 200
                .andExpect(MockMvcResultMatchers.status().isOk())
                // code in createUserResponse
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("result.message").value("Create user success!"));
    }

    @Test
    void register_invalidUsername_fail() throws Exception {
        // Given

        // set username invalid
        createUserRequest.setUsername("A");

        // Sử dụng ObjectMapper để chuyển nội dung trong "createUserRequest" thành String
        ObjectMapper objectMapper = new ObjectMapper();
        // Thông báo cho Json khi add thư viện serialize LocalDate to Json
        objectMapper.registerModule(new JavaTimeModule());

        String content = objectMapper.writeValueAsString(createUserRequest);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register") // request method in postman
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                // Then
                // HttpStatusCode = 200
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                // code in createUserResponse
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 3 characters"));
    }
}
