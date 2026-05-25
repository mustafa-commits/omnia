package com.sc.demo.service.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    private final JwtEncoder encoder;

    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    private JwtDecoder jwtDecoder;

    public String generateToken(String Id) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.DAYS))
                .subject(Id)
                .claim("requestId", "10272")
                .claim("headFamilyId", "71120")
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    public Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }

}
