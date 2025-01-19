package com.shongon.identity_service.dto.request.permission;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePermissionRequest {
    String permission_name;
    String permission_description;
}
