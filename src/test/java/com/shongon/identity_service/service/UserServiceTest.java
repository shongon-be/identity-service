package com.shongon.identity_service.service;

import com.shongon.identity_service.dto.request.user.CreateUserRequest;
import com.shongon.identity_service.dto.response.user.CreateUserResponse;
import com.shongon.identity_service.entity.Role;
import com.shongon.identity_service.entity.User;
import com.shongon.identity_service.exception.AppException;
import com.shongon.identity_service.mapper.UserMapper;
import com.shongon.identity_service.repository.RoleRepository;
import com.shongon.identity_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private UserMapper userMapper;

    private User user;
    private Role role;
    private CreateUserRequest request;
    private CreateUserResponse response;

    @BeforeEach
    void setup() {
        var birthDate = LocalDate.of(1990, 1, 1);
        role = Role.builder()
                .role_name("USER")
                .role_description("ROLE_USER")
                .build();

        request = CreateUserRequest.builder()
                .username("test")
                .firstName("test")
                .lastName("test")
                .password("12345678")
                .birthDate(birthDate)
                .build();

        response = CreateUserResponse.builder()
                .message("Create user success!")
                .build();

        user = User.builder()
                .id("1231273612")
                .username("test")
                .firstName("test")
                .lastName("test")
                .birthDate(birthDate)
                .roles(Set.of(role))
                .build();

        mockDependencies();
    }

    // Phải khai báo rõ các mock như userMapper, roleRepository để đảm bảo các phương thức hoạt động đúng
    private void mockDependencies() {
        when(roleRepository.findById("USER")).thenReturn(Optional.of(role));
        when(userMapper.createUser(any())).thenReturn(user);
        when(userMapper.toCreateUserResponse(any())).thenReturn(response);
    }

    @Test
    void createUser_validRequest_success() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // When
        var result = userService.createUser(request);

        // Then
        assertThat(result.getMessage()).isEqualTo("Create user success!");
    }

    @Test
    void createUser_userExisted_failed() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When
        var ex = assertThrows(AppException.class, () -> userService.createUser(request));

        // Then
        assertThat(ex.getErrorCode().getCode()).isEqualTo(400);
        assertThat(ex.getMessage()).isEqualTo("User already existed");
    }
}
