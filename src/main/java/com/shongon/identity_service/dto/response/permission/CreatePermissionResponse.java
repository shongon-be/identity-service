package com.shongon.identity_service.dto.response.permission;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePermissionResponse {
    String message = "Create permission success!";
}
