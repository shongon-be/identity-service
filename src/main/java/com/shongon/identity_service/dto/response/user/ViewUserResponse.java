package com.shongon.identity_service.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewUserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate birthDate;
    Set<String> roles;

}
