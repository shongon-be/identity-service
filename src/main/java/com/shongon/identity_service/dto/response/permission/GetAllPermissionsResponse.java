package com.shongon.identity_service.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllPermissionsResponse {
    String permission_name;
    String permission_description;
}
