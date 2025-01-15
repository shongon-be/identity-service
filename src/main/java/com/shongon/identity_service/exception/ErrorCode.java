package com.shongon.identity_service.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(1000, "Uncategorized Error"),
    INVALID_KEY(1001, "Invalid Message Key"),
    INVALID_TOKEN(1002, "Invalid Token"),

    USER_EXISTED(400, "User already existed"),
    USER_NOT_FOUND(404, "User not found"),
    USER_CREATION_FAILED(400, "User creation failed"),

    INVALID_USERNAME(400, "Username must be at least 3 characters"),
    INVALID_PASSWORD(400, "Password must be at least 8 characters"),
    UNAUTHENTICATED(400, "Unauthenticated Error"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;


}
