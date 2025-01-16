package com.shongon.identity_service.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewUserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate birthDate;
}
