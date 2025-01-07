package com.shongon.identity_service.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shongon.identity_service.config.CustomLocalDateTimeSerializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserResponse {
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    LocalDateTime createdAt;
}
