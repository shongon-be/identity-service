package com.shongon.identity_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.shongon.identity_service.dto.request.user.CreateUserRequest;
import com.shongon.identity_service.dto.response.user.CreateUserResponse;
import com.shongon.identity_service.dto.response.user.ViewUserResponse;
import com.shongon.identity_service.entity.Role;
import com.shongon.identity_service.entity.User;
import com.shongon.identity_service.exception.AppException;
import com.shongon.identity_service.mapper.UserMapper;
import com.shongon.identity_service.repository.RoleRepository;
import com.shongon.identity_service.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@TestPropertySource("/test.properties")
class UserServiceTest {
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
    private CreateUserRequest createUserRequest;
    private CreateUserResponse createUserResponse;

    @BeforeEach
    void setup() {
        var birthDate = LocalDate.of(1990, 1, 1);
        role = Role.builder().role_name("USER").role_description("ROLE_USER").build();

        createUserRequest = CreateUserRequest.builder()
                .username("test")
                .firstName("test")
                .lastName("test")
                .password("12345678")
                .birthDate(birthDate)
                .build();

        createUserResponse =
                CreateUserResponse.builder().message("Create user success!").build();

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
        when(userMapper.toCreateUserResponse(any())).thenReturn(createUserResponse);
    }

    @Test
    void createUser_validRequest_success() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // When
        var result = userService.createUser(createUserRequest);

        // Then
        assertThat(result.getMessage()).isEqualTo("Create user success!");
    }

    @Test
    void createUser_userExisted_error() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When
        var ex = assertThrows(AppException.class, () -> userService.createUser(createUserRequest));

        // Then
        assertThat(ex.getErrorCode().getCode()).isEqualTo(400);
        assertThat(ex.getMessage()).isEqualTo("User already existed");
    }

    @Test
    @WithMockUser(username = "test")
    void getMyInfo_validRequest_success() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        when(userMapper.toViewUserResponse(any()))
                .thenReturn(ViewUserResponse.builder()
                        .username("test")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .build());

        // When
        var response = userService.getMyInfo();

        // Then
        assertThat(response.getUsername()).isEqualTo("test");
        assertThat(response.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    @WithMockUser(username = "hongson")
    void getMyInfo_invalidUserRequest_failed() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userMapper.toViewUserResponse(any()))
                .thenReturn(ViewUserResponse.builder()
                        .username("test")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .build());

        // When & Then
        assertThrows(AuthorizationDeniedException.class, () -> userService.getMyInfo());
    }

    @Test
    @WithMockUser(username = "test")
    void getMyInfo_userNotFound_error() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When
        var ex = assertThrows(AppException.class, () -> userService.getMyInfo());

        // Then
        assertThat(ex.getErrorCode().getCode()).isEqualTo(404);
        assertThat(ex.getMessage()).isEqualTo("User not found");
    }
}
