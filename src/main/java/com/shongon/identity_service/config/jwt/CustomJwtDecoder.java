package com.shongon.identity_service.config.jwt;

import com.nimbusds.jose.JOSEException;
import com.shongon.identity_service.dto.request.auth.IntrospectRequest;
import com.shongon.identity_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Autowired
    private AuthService authService;

    private NimbusJwtDecoder jwtDecoder = null;

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Override
    public Jwt decode (String token) throws JwtException{
        try {
            var response = authService.introspect(IntrospectRequest.builder()
                    .token(token)
                    .build());

            if (!response.isValid())
                throw new JwtException("Invalid token");
        } catch (JOSEException | ParseException e){
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(jwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            jwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return jwtDecoder.decode(token);
    }
}
