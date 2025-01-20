package com.shongon.identity_service.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shongon.identity_service.dto.response.role.GetAllRolesResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewUserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate birthDate;
    Set<GetAllRolesResponse> roles;
}
