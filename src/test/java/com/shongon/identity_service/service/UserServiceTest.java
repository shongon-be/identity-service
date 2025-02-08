package com.shongon.identity_service.service;

import com.shongon.identity_service.dto.request.user.CreateUserRequest;
import com.shongon.identity_service.dto.response.user.CreateUserResponse;
import com.shongon.identity_service.entity.User;
import com.shongon.identity_service.exception.AppException;
import com.shongon.identity_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private CreateUserRequest createUserRequest;
    private CreateUserResponse createUserResponse;

    @BeforeEach
    void initData(){
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
        createUserResponse = CreateUserResponse.builder()
                .message("Create user success!")
                .build();

        // sample User
        user = User.builder()
                .id("1231273612")
                .username("test")
                .firstName("test")
                .lastName("test")
                .birthDate(birthDate)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // Given
        when(userRepository.existsByUsername(anyString()))
            .thenReturn(false);

        when(userRepository.save(any()))
                .thenReturn(user);

        // When
        var response = userService.createUser(createUserRequest);

        // Then
        assertThat(response.getMessage()).isEqualTo("Create user success!");
    }

    @Test
    void createUser_userExisted_failed() throws Exception {
        // Given
        when(userRepository.existsByUsername(anyString()))
                .thenReturn(true);

        // When
       var exception = assertThrows(AppException.class, () -> userService.createUser(createUserRequest));

       assertThat(exception.getErrorCode().getCode()).isEqualTo(400);
       assertThat(exception.getMessage()).isEqualTo("User already existed");
    }
}
