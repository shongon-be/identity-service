package com.shongon.identity_service.controller;

import com.shongon.identity_service.dto.request.CreateUserRequest;
import com.shongon.identity_service.dto.request.UpdateUserRequest;
import com.shongon.identity_service.dto.response.*;
import com.shongon.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<CreateUserResponse> register(@RequestBody @Valid CreateUserRequest request) {
        ApiResponse<CreateUserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping("/view-all")
    public ApiResponse<List<GetAllUsersResponse>> getAllUsers() {
        ApiResponse<List<GetAllUsersResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllUsers());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    public ApiResponse<ViewUserResponse> getUserById(@PathVariable String userId) {
        ApiResponse<ViewUserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUserById(userId));
        return apiResponse;
    }

    @PutMapping("/update/{userId}")
    public ApiResponse<UpdateUserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UpdateUserRequest request) {
        ApiResponse<UpdateUserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(userId, request));
        return apiResponse;
    }

    @DeleteMapping("/delete/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        userService.deleteUser(userId);
        apiResponse.setResult("User has been deleted!");
        return apiResponse;
    }
}
