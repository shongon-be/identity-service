package com.shongon.identity_service.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    @Size(min = 8, message = "INVALID_PASSWORD")
    @NotBlank
    String password;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @NotNull
    LocalDate birthDate;

    List<String> roles;
}
