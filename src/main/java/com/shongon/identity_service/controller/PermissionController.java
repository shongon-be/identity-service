package com.shongon.identity_service.controller;

import com.shongon.identity_service.dto.request.permission.CreatePermissionRequest;
import com.shongon.identity_service.dto.response.permission.CreatePermissionResponse;
import com.shongon.identity_service.dto.response.permission.GetAllPermissionsResponse;
import com.shongon.identity_service.dto.response.user.ApiResponse;
import com.shongon.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/permissions")
public class PermissionController {
    PermissionService permissionService;

    @PostMapping("/create")
    public ApiResponse<CreatePermissionResponse> createPermission (@RequestBody CreatePermissionRequest request) {
        return ApiResponse.<CreatePermissionResponse>builder()
                .code(200)
                .result(permissionService.createPermission(request))
                .build();
    }

    @GetMapping("/view-all")
    public ApiResponse<List<GetAllPermissionsResponse>> viewAllPermissions() {
        return ApiResponse.<List<GetAllPermissionsResponse>>builder()
                .code(200)
                .result(permissionService.getAllPermissions())
                .build();
    }

    @DeleteMapping("/delete/{permission_name}")
    public ApiResponse<String> deletePermission(@PathVariable String permission_name) {
        permissionService.deletePermission(permission_name);
        return ApiResponse.<String>builder()
                .code(200)
                .result("Permission has been deleted")
                .build();
    }
}
