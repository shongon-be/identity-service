package com.shongon.identity_service.service;

import com.shongon.identity_service.dto.request.user.CreateUserRequest;
import com.shongon.identity_service.dto.request.user.UpdateUserRequest;
import com.shongon.identity_service.dto.response.user.CreateUserResponse;
import com.shongon.identity_service.dto.response.user.GetAllUsersResponse;
import com.shongon.identity_service.dto.response.user.UpdateUserResponse;
import com.shongon.identity_service.dto.response.user.ViewUserResponse;
import com.shongon.identity_service.entity.User;
import com.shongon.identity_service.exception.AppException;
import com.shongon.identity_service.exception.ErrorCode;
import com.shongon.identity_service.mapper.UserMapper;
import com.shongon.identity_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        validateUsername(request.getUsername());

        User user = userMapper.createUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toCreateUserResponse(userRepository.save(user));

    }

    @Transactional
    public List<GetAllUsersResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toGetAllUserResponse)
                .toList();
    }

    @Transactional
    public ViewUserResponse getUserById(String id) {
        return userMapper.toViewUserResponse(userRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public UpdateUserResponse updateUser(String userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);

        return userMapper.toUpdateUserResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId))
            throw new AppException(ErrorCode.USER_NOT_FOUND);

        userRepository.deleteById(userId);
    }

    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username))
            throw new AppException(ErrorCode.USER_EXISTED);
    }
}
