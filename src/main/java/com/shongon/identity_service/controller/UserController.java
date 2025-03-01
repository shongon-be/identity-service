package com.shongon.identity_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.shongon.identity_service.dto.request.user.CreateUserRequest;
import com.shongon.identity_service.dto.request.user.UpdateUserRequest;
import com.shongon.identity_service.dto.response.user.*;
import com.shongon.identity_service.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @GetMapping("/my-info")
    public ApiResponse<ViewUserResponse> getMyInfo() {
        return ApiResponse.<ViewUserResponse>builder()
                .code(200)
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping("/view-all")
    public ApiResponse<List<GetAllUsersResponse>> getAllUsers() {
        return ApiResponse.<List<GetAllUsersResponse>>builder()
                .code(200)
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<ViewUserResponse> getUserById(@PathVariable String userId) {
        return ApiResponse.<ViewUserResponse>builder()
                .code(200)
                .result(userService.getUserById(userId))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<CreateUserResponse> register(@RequestBody @Valid CreateUserRequest request) {
        log.info("Controller: create User");

        return ApiResponse.<CreateUserResponse>builder()
                .code(200)
                .result(userService.createUser(request))
                .build();
    }

    @PutMapping("/update/{userId}")
    public ApiResponse<UpdateUserResponse> updateUser(
            @PathVariable String userId, @RequestBody @Valid UpdateUserRequest request) {
        return ApiResponse.<UpdateUserResponse>builder()
                .code(200)
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/delete/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .code(200)
                .result("User has been deleted!")
                .build();
    }
}
