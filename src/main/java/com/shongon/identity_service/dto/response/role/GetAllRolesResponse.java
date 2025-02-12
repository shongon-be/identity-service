package com.shongon.identity_service.dto.response.role;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shongon.identity_service.dto.response.permission.GetAllPermissionsResponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllRolesResponse {
    String role_name;
    String role_description;
    Set<GetAllPermissionsResponse> permissions;
}
