package com.shongon.identity_service.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shongon.identity_service.utils.CustomTime;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllUsersResponse {

    String id;

    String username;

    String firstName;

    String lastName;

    LocalDate birthDate;

    Set<String> roles;

    @JsonSerialize(using = CustomTime.class)
    LocalDateTime createdAt;

    @JsonSerialize(using = CustomTime.class)
    LocalDateTime updatedAt;
}
