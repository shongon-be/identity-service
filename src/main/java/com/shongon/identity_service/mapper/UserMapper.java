package com.shongon.identity_service.mapper;

import com.shongon.identity_service.dto.request.CreateUserRequest;
import com.shongon.identity_service.dto.request.UpdateUserRequest;
import com.shongon.identity_service.dto.response.CreateUserResponse;
import com.shongon.identity_service.dto.response.GetAllUsersResponse;
import com.shongon.identity_service.dto.response.UpdateUserResponse;
import com.shongon.identity_service.dto.response.ViewUserResponse;
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
