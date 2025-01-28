package com.shongon.identity_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shongon.identity_service.dto.request.auth.AuthenticationRequest;
import com.shongon.identity_service.dto.request.auth.IntrospectRequest;
import com.shongon.identity_service.dto.request.auth.LogoutRequest;
import com.shongon.identity_service.dto.response.auth.AuthenticationResponse;
import com.shongon.identity_service.dto.response.auth.IntrospectResponse;
import com.shongon.identity_service.entity.InvalidatedToken;
import com.shongon.identity_service.entity.User;
import com.shongon.identity_service.exception.AppException;
import com.shongon.identity_service.exception.ErrorCode;
import com.shongon.identity_service.repository.InvalidatedTokenRepository;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    InvalidatedTokenRepository tokenRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    //  Introspect token
    @Transactional
    public IntrospectResponse introspect(IntrospectRequest introspectRequest)
            throws JOSEException, ParseException {

        var token = introspectRequest.getToken();

        boolean isValid = true;

        try {
            verifyToken(token);
        }catch (AppException e){
           isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

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

    // Log out & disable token
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        var signToken = verifyToken(logoutRequest.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();

        Date expTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expTime(expTime)
                .build();

        tokenRepository.save(invalidatedToken);
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
                .jwtID(UUID.randomUUID().toString())
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

    // Build content of token - Payload
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

    // Verify valid token
    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expTime.after(new Date())))
            throw new AppException(ErrorCode.INVALID_TOKEN);

        // verify if token is logged out
        var invalidateToken = tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID());

        if (invalidateToken)
            throw new AppException(ErrorCode.INVALID_TOKEN);

        return signedJWT;
    }
}
