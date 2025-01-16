package com.shongon.identity_service.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserResponse {
    String message = "Update success!";
}
