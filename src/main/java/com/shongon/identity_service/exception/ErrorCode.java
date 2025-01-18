package com.shongon.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(500, HttpStatus.INTERNAL_SERVER_ERROR,"Uncategorized Error"),
    INVALID_KEY(400, HttpStatus.BAD_REQUEST,"Invalid Message Key"),

    USER_EXISTED(400, HttpStatus.BAD_REQUEST,"User already existed"),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND,"User not found"),

    INVALID_USERNAME(400, HttpStatus.BAD_REQUEST,"Username must be at least 3 characters"),
    INVALID_PASSWORD(400, HttpStatus.BAD_REQUEST,"Password must be at least 8 characters"),

    LOGIN_FAILED(401, HttpStatus.UNAUTHORIZED,"Username or password is incorrect"),
    INVALID_TOKEN(401, HttpStatus.UNAUTHORIZED,"Invalid Token"),
    INVALID_PERMISSION(403, HttpStatus.FORBIDDEN,"You do not have permission to access this resource"),
    ;

    ErrorCode(int code, HttpStatusCode statusCode,String message) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;

}
