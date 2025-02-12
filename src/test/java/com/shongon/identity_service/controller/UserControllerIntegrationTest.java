package com.shongon.identity_service.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shongon.identity_service.dto.request.user.CreateUserRequest;
import com.shongon.identity_service.dto.response.user.CreateUserResponse;
import com.shongon.identity_service.entity.Role;
import com.shongon.identity_service.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {

    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", MY_SQL_CONTAINER::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    private CreateUserRequest createUserRequest;
    private CreateUserResponse createUserResponse;

    @BeforeEach
    void initData() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        // Setup default role
        Role userRole = Role.builder().role_name("USER").build();
        roleRepository.save(userRole);
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

        // When
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/users/register") // request method in postman
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                // Then
                // HttpStatusCode = 200
                .andExpect(MockMvcResultMatchers.status().isOk())
                // code in createUserResponse
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("result.message").value("Create user success!"));

        log.info("Result: {}", response.andReturn().getResponse().getContentAsString());
    }
}
