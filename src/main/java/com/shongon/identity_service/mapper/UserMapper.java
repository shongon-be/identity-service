package com.shongon.identity_service.mapper;

import com.shongon.identity_service.dto.request.user.CreateUserRequest;
import com.shongon.identity_service.dto.request.user.UpdateUserRequest;
import com.shongon.identity_service.dto.response.user.CreateUserResponse;
import com.shongon.identity_service.dto.response.user.GetAllUsersResponse;
import com.shongon.identity_service.dto.response.user.UpdateUserResponse;
import com.shongon.identity_service.dto.response.user.ViewUserResponse;
import com.shongon.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User createUser(CreateUserRequest request);

    void updateUser(@MappingTarget User user, UpdateUserRequest request);

    CreateUserResponse toCreateUserResponse(User user);

    UpdateUserResponse toUpdateUserResponse(User user);

    GetAllUsersResponse toGetAllUserResponse(User user);

    ViewUserResponse toViewUserResponse(User user);
}
