package com.shongon.identity_service.mapper;

import com.shongon.identity_service.dto.request.role.CreateRoleRequest;
import com.shongon.identity_service.dto.request.role.UpdateRoleRequest;
import com.shongon.identity_service.dto.response.role.CreateRoleResponse;
import com.shongon.identity_service.dto.response.role.GetAllRolesResponse;
import com.shongon.identity_service.dto.response.role.UpdateRoleResponse;
import com.shongon.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toCreateRole(CreateRoleRequest request);

    @Mapping(target = "permissions", ignore = true)
    void toUpdateRole(@MappingTarget Role role, UpdateRoleRequest updateRequest);

    CreateRoleResponse toCreateRoleResponse(Role role);

    GetAllRolesResponse toGetAllRolesResponse(Role role);

    UpdateRoleResponse toUpdateRoleResponse(Role role);
}
