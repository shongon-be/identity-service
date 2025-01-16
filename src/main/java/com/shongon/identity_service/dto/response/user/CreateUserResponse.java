package com.shongon.identity_service.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserResponse {
    String message = "Create user success!";
}
