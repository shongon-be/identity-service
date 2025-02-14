package com.shongon.identity_service.exception;

import java.text.ParseException;
import java.util.Map;

import jakarta.validation.ConstraintViolation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.shongon.identity_service.dto.response.user.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    //    Handling unexpected error
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handlingUncategorizedException(RuntimeException e) {
        log.error("Exception: ", e);

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED.getMessage());

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED.getStatusCode()).body(apiResponse);
    }

    //    Handling invalid token
    @ExceptionHandler(value = ParseException.class)
    public ResponseEntity<ApiResponse> handlingInvalidToken(ParseException e) {
        log.error("Invalid/Expired Token: ", e);

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.INVALID_TOKEN.getCode());
        apiResponse.setMessage(ErrorCode.INVALID_TOKEN.getMessage());

        return ResponseEntity.status(ErrorCode.INVALID_TOKEN.getStatusCode()).body(apiResponse);
    }

    //    Handling invalid permission
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handlingInvalidPermission(AccessDeniedException e) {
        log.error("Permission denied: ", e);

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.INVALID_PERMISSION.getCode());
        apiResponse.setMessage(ErrorCode.INVALID_PERMISSION.getMessage());

        return ResponseEntity.status(ErrorCode.INVALID_PERMISSION.getStatusCode())
                .body(apiResponse);
    }

    //    Handling application exceptions
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException(AppException e) {
        log.error("AppException: ", e);

        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    //    Handling typo error in validation
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException e) {

        String enumKey = e.getBindingResult().getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);

            var constraintViolations =
                    e.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);

            attributes = constraintViolations.getConstraintDescriptor().getAttributes();

            log.warn(attributes.toString());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(mapAttribute(errorCode.getMessage(), attributes));

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attribute) {
        String minValue = attribute.get(MIN_ATTRIBUTE).toString();

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
