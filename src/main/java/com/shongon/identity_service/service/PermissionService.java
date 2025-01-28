package com.shongon.identity_service.service;

import com.shongon.identity_service.dto.request.permission.CreatePermissionRequest;
import com.shongon.identity_service.dto.response.permission.CreatePermissionResponse;
import com.shongon.identity_service.dto.response.permission.GetAllPermissionsResponse;
import com.shongon.identity_service.entity.Permission;
import com.shongon.identity_service.exception.AppException;
import com.shongon.identity_service.exception.ErrorCode;
import com.shongon.identity_service.mapper.PermissionMapper;
import com.shongon.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class PermissionService {
    PermissionRepository permissionRepo;
    PermissionMapper permissionMapper;

    // Create permission
    @Transactional
    public CreatePermissionResponse createPermission(CreatePermissionRequest request) {
        if (permissionRepo.existsById(request.getPermission_name()))
            throw new AppException(ErrorCode.PERMISSION_EXISTED);

        Permission permission = permissionMapper.createPermission(request);


        return permissionMapper.toCreatePermissionResponse(permissionRepo.save(permission));
    }

    //Get list of permissions
    @Transactional
    public List<GetAllPermissionsResponse> getAllPermissions() {
        return permissionRepo.findAll()
                .stream()
                .map(permissionMapper::toGetAllPermissionsResponse)
                .toList();
    }

    @Transactional
    public void deletePermission(String permission_name){
        if (!permissionRepo.existsById(permission_name))
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);

        permissionRepo.deleteById(permission_name);
    }
}
