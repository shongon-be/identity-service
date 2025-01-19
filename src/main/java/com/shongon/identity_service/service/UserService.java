package com.shongon.identity_service.service;

import com.shongon.identity_service.dto.request.user.CreateUserRequest;
import com.shongon.identity_service.dto.request.user.UpdateUserRequest;
import com.shongon.identity_service.dto.response.user.CreateUserResponse;
import com.shongon.identity_service.dto.response.user.GetAllUsersResponse;
import com.shongon.identity_service.dto.response.user.UpdateUserResponse;
import com.shongon.identity_service.dto.response.user.ViewUserResponse;
import com.shongon.identity_service.entity.User;
import com.shongon.identity_service.enums.Role;
import com.shongon.identity_service.exception.AppException;
import com.shongon.identity_service.exception.ErrorCode;
import com.shongon.identity_service.mapper.UserMapper;
import com.shongon.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @PostAuthorize("returnObject.username == authentication.name")
    @Transactional(readOnly = true)
    public ViewUserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String whoIsLogged = context.getAuthentication().getName();

        User info = userRepository.findByUsername(whoIsLogged).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toViewUserResponse(info);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<GetAllUsersResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toGetAllUserResponse)
                .toList();
    }

    // Kiểm tra xem user đang login có đang lấy đúng id của minh không
    // @PostAuthorize("returnObject.username == authentication.name")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public ViewUserResponse getUserById(String id) {
        return userMapper.toViewUserResponse(userRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        validateUsername(request.getUsername());

        User user = userMapper.createUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

//        HashSet<String> roles = new HashSet<>();
//        roles.add(Role.USER.name());
//       user.setRoles(roles);

        return userMapper.toCreateUserResponse(userRepository.save(user));

    }

    @PreAuthorize("@userRepository.findById(#userId).get().username == authentication.name || hasRole('ADMIN')")
    @Transactional
    public UpdateUserResponse updateUser(String userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);

        return userMapper.toUpdateUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
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
