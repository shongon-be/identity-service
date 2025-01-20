package com.shongon.identity_service.mapper;

import com.shongon.identity_service.dto.request.user.CreateUserRequest;
import com.shongon.identity_service.dto.request.user.UpdateUserRequest;
import com.shongon.identity_service.dto.response.user.CreateUserResponse;
import com.shongon.identity_service.dto.response.user.GetAllUsersResponse;
import com.shongon.identity_service.dto.response.user.UpdateUserResponse;
import com.shongon.identity_service.dto.response.user.ViewUserResponse;
import com.shongon.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User createUser(CreateUserRequest request);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UpdateUserRequest request);

    CreateUserResponse toCreateUserResponse(User user);

    UpdateUserResponse toUpdateUserResponse(User user);

    GetAllUsersResponse toGetAllUserResponse(User user);

    ViewUserResponse toViewUserResponse(User user);
}
