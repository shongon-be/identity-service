package com.shongon.identity_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shongon.identity_service.dto.request.auth.AuthenticationRequest;
import com.shongon.identity_service.dto.request.auth.IntrospectRequest;
import com.shongon.identity_service.dto.response.auth.AuthenticationResponse;
import com.shongon.identity_service.dto.response.auth.IntrospectResponse;
import com.shongon.identity_service.entity.User;
import com.shongon.identity_service.exception.AppException;
import com.shongon.identity_service.exception.ErrorCode;
import com.shongon.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

//    Login & get token
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        var user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isAuthenticated =  passwordEncoder.matches(authRequest.getPassword()
                , user.getPassword());

        if (!isAuthenticated)
            throw new AppException(ErrorCode.LOGIN_FAILED);

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .token(token)
                .build();
    }

//  Introspect token
    @Transactional
    public IntrospectResponse introspect(IntrospectRequest introspectRequest)
            throws JOSEException, ParseException {
        var token = introspectRequest.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expTime.after(new Date()))
                .build();
    }


//    Generate token
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("com.shongon")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope",buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (Exception e) {
            log.error("Can not create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role ->{

                stringJoiner.add("ROLE_" + role.getRole_name());

                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions()
                            .forEach(permission -> stringJoiner.add(permission.getPermission_name()));
            });

        return stringJoiner.toString();
    }
}
