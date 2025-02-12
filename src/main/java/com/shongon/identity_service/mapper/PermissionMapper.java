package com.shongon.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.shongon.identity_service.dto.request.permission.CreatePermissionRequest;
import com.shongon.identity_service.dto.response.permission.CreatePermissionResponse;
import com.shongon.identity_service.dto.response.permission.GetAllPermissionsResponse;
import com.shongon.identity_service.entity.Permission;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {
    Permission createPermission(CreatePermissionRequest request);

    CreatePermissionResponse toCreatePermissionResponse(Permission permission);

    GetAllPermissionsResponse toGetAllPermissionsResponse(Permission permission);
}
