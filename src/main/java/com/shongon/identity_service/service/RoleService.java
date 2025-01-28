package com.shongon.identity_service.service;

import com.shongon.identity_service.dto.request.role.CreateRoleRequest;
import com.shongon.identity_service.dto.request.role.UpdateRoleRequest;
import com.shongon.identity_service.dto.response.role.CreateRoleResponse;
import com.shongon.identity_service.dto.response.role.GetAllRolesResponse;
import com.shongon.identity_service.dto.response.role.UpdateRoleResponse;
import com.shongon.identity_service.entity.Role;
import com.shongon.identity_service.exception.AppException;
import com.shongon.identity_service.exception.ErrorCode;
import com.shongon.identity_service.mapper.RoleMapper;
import com.shongon.identity_service.repository.PermissionRepository;
import com.shongon.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class RoleService {
    RoleRepository roleRepo;
    RoleMapper roleMapper;
    PermissionRepository permissionRepo;

    @Transactional
    public CreateRoleResponse createRole(CreateRoleRequest request) {
        if(roleRepo.existsById(request.getRole_name()))
            throw new AppException(ErrorCode.ROLE_EXISTED);

        Role role = roleMapper.toCreateRole(request);

        var permissions = permissionRepo.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toCreateRoleResponse(roleRepo.save(role));
    }

    @Transactional(readOnly = true)
    public List<GetAllRolesResponse> getAllRoles() {
        return roleRepo.findAll()
                .stream()
                .map(roleMapper::toGetAllRolesResponse)
                .toList();
    }

    @Transactional
    public UpdateRoleResponse updateRolePermissions(String role_name, UpdateRoleRequest request) {
        Role role = roleRepo.findById(role_name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        roleMapper.toUpdateRole(role, request);

        var permissions = permissionRepo.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toUpdateRoleResponse(roleRepo.save(role));
    }

    @Transactional
    public void deleteRole(String role_name) {
        if(!roleRepo.existsById(role_name))
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);

        roleRepo.deleteById(role_name);
    }
}
