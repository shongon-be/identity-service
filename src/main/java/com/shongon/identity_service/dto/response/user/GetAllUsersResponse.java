package com.shongon.identity_service.dto.response.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shongon.identity_service.config.CustomLocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllUsersResponse {

    String id;

    String username;

    String firstName;

    String lastName;

    LocalDate birthDate;

    Set<String> roles;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    LocalDateTime createdAt;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    LocalDateTime updatedAt;
}
