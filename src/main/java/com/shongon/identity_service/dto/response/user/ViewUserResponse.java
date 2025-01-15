package com.shongon.identity_service.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewUserResponse {
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate birthDate;
}
