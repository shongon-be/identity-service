package com.shongon.identity_service.service;

import com.shongon.identity_service.dto.request.CreateUserRequest;
import com.shongon.identity_service.dto.request.UpdateUserRequest;
import com.shongon.identity_service.dto.response.CreateUserResponse;
import com.shongon.identity_service.dto.response.GetAllUsersResponse;
import com.shongon.identity_service.dto.response.UpdateUserResponse;
import com.shongon.identity_service.dto.response.ViewUserResponse;
import com.shongon.identity_service.entity.User;
import com.shongon.identity_service.exception.AppException;
import com.shongon.identity_service.exception.ErrorCode;
import com.shongon.identity_service.mapper.UserMapper;
import com.shongon.identity_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        validateUsername(request.getUsername());

       return Optional.of(request)
               .map(userMapper::createUser)
               .map(userRepository::save)
               .map(userMapper::toCreateUserResponse)
               .orElseThrow(() ->  new AppException(ErrorCode.USER_CREATION_FAILED));
    }

    @Transactional
    public List<GetAllUsersResponse> getAllUsers() {
        return userRepository.findAll()
                .parallelStream()
                .map(userMapper::toGetAllUserResponse)
                .toList();
    }

    @Transactional
    public ViewUserResponse getUserById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toViewUserResponse)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public UpdateUserResponse updateUser(String userId, UpdateUserRequest request) {
        return userRepository.findById(userId)
                .map(user ->
                {
                    userMapper.updateUser(user,request);
                    return userRepository.save(user);
                })
                .map(userMapper::toUpdateUserResponse)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
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
