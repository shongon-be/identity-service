package com.shongon.identity_service.dto.request.user;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.shongon.identity_service.utils.validation.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @DobConstraint(min = 18, message = "INVALID_DOB")
    @NotNull
    LocalDate birthDate;

    List<String> roles;
}
