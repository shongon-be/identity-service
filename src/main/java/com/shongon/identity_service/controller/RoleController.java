package com.shongon.identity_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.shongon.identity_service.dto.request.role.CreateRoleRequest;
import com.shongon.identity_service.dto.request.role.UpdateRoleRequest;
import com.shongon.identity_service.dto.response.role.CreateRoleResponse;
import com.shongon.identity_service.dto.response.role.GetAllRolesResponse;
import com.shongon.identity_service.dto.response.role.UpdateRoleResponse;
import com.shongon.identity_service.dto.response.user.ApiResponse;
import com.shongon.identity_service.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;

    @PostMapping("/create")
    public ApiResponse<CreateRoleResponse> createRole(@RequestBody CreateRoleRequest request) {
        return ApiResponse.<CreateRoleResponse>builder()
                .code(200)
                .result(roleService.createRole(request))
                .build();
    }

    @GetMapping("view-all")
    public ApiResponse<List<GetAllRolesResponse>> getAllRoles() {
        return ApiResponse.<List<GetAllRolesResponse>>builder()
                .code(200)
                .result(roleService.getAllRoles())
                .build();
    }

    @PutMapping("/update/{role_name}")
    public ApiResponse<UpdateRoleResponse> updateRole(
            @PathVariable String role_name, @RequestBody UpdateRoleRequest request) {

        return ApiResponse.<UpdateRoleResponse>builder()
                .code(200)
                .result(roleService.updateRolePermissions(role_name, request))
                .build();
    }

    @DeleteMapping("/delete/{role_name}")
    public ApiResponse<String> deleteRole(@PathVariable String role_name) {
        roleService.deleteRole(role_name);

        return ApiResponse.<String>builder()
                .code(200)
                .result("Role has been deleted!")
                .build();
    }
}
