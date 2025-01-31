package com.shongon.identity_service.controller;

import com.nimbusds.jose.JOSEException;
import com.shongon.identity_service.dto.request.auth.AuthenticationRequest;
import com.shongon.identity_service.dto.request.auth.IntrospectRequest;
import com.shongon.identity_service.dto.request.auth.LogoutRequest;
import com.shongon.identity_service.dto.request.auth.RefreshRequest;
import com.shongon.identity_service.dto.response.auth.AuthenticationResponse;
import com.shongon.identity_service.dto.response.auth.IntrospectResponse;
import com.shongon.identity_service.dto.response.user.ApiResponse;
import com.shongon.identity_service.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthController {
    AuthService authService;

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {

        return ApiResponse.<IntrospectResponse>builder()
                .code(200)
                .result(authService.introspect(introspectRequest))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .result(authService.authenticate(authRequest))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest)
            throws ParseException, JOSEException {

        authService.logout(logoutRequest);

        return ApiResponse.<Void>builder()
                .code(200)
                .build();
    }

    @PostMapping("refresh")
    public ApiResponse<AuthenticationResponse> refreshToken (@RequestBody RefreshRequest refreshRequest)
            throws ParseException, JOSEException {

        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .result(authService.refreshToken(refreshRequest))
                .build();
    }
}
