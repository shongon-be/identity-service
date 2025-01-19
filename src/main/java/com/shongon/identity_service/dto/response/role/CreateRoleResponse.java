package com.shongon.identity_service.dto.response.role;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRoleResponse {
    String message = "Create role success!";
}
