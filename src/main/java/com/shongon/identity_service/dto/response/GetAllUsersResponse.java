package com.shongon.identity_service.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shongon.identity_service.config.CustomLocalDateTimeSerializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllUsersResponse {

    String id;

    String username;

    String password;

    String firstName;

    String lastName;

    LocalDate birthDate;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    LocalDateTime createdAt;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    LocalDateTime updatedAt;
}
