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

    // انشاء توكت مستخدمي التطبيق
    public String generateToken(String userId, Long requestId, Long headFamilyId, String branches) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("https://aynyateem.com/")
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.DAYS))
                .subject(userId)
                .claim("requestId", requestId)
                .claim("headFamilyId", headFamilyId)
                .claim("branches", branches)
                .claim("scope", "APP")
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    // انشاء توكن مستخدمي الداش بورد
    public String generateEmployeesToken(String userDashboardId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("https://aynyateem.com/")
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.DAYS))
                .subject(userDashboardId)
                .claim("scope", "DASHBOARD")
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }

}
