package com.shongon.identity_service.controller;

import com.shongon.identity_service.dto.request.CreateUserRequest;
import com.shongon.identity_service.dto.request.UpdateUserRequest;
import com.shongon.identity_service.dto.response.ApiResponse;
import com.shongon.identity_service.entity.User;
import com.shongon.identity_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody @Valid CreateUserRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.createUser(request));

        return apiResponse;
    }

    @GetMapping("/view-all")
    public ApiResponse<List<User>> getAllUsers() {
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.getAllUsers());

        return apiResponse;
    }

    @GetMapping("/{userId}")
    public ApiResponse<User> getUserById(@PathVariable String userId) {
        ApiResponse<User> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.getUserById(userId));

        return apiResponse;
    }

    @PutMapping("/update/{userId}")
    public ApiResponse<User> updateUser(@PathVariable String userId, @RequestBody UpdateUserRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();

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
