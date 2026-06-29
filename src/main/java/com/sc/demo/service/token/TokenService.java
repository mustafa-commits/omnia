package com.sc.demo.service.token;

import com.sc.demo.model.chat.Platform;
import com.sc.demo.model.dto.token.TokenRequest;
import com.sc.demo.model.token.AppToken;
import com.sc.demo.repository.chat.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class TokenService {
    private final JwtEncoder encoder;

    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private TokenRepo tokenRepo;

    // انشاء توكت مستخدمي التطبيق
    public String generateToken(String userId, Long requestId, Long headFamilyId, String branches) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("https://aynyateem.com/")
                .issuedAt(now)
                .expiresAt(now.atZone(ZoneOffset.UTC)
                        .plusYears(2)
                        .toInstant())
                .subject(userId)
                .claim("requestId", requestId)
                .claim("headFamilyId", headFamilyId)
                .claim("branches", branches)
                .claim("scope", "APP")
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    // انشاء توكن مستخدمي الداش بورد
    public String generateUserDashboardToken(String userDashboardId, String userName, Long groupId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("https://aynyateem.com/")
                .issuedAt(now)
                .expiresAt(now.atZone(ZoneOffset.UTC)
                        .plusYears(2)
                        .toInstant())
                .subject(userDashboardId)
                .claim("groupId", groupId)
                .claim("userName", userName)
                .claim("scope", "DASHBOARD")
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }

    // حفظ Token القادم من firebase في قاعدة البيانات
    public long saveToken(TokenRequest tokenRequest) {
        Optional<AppToken> byToken = tokenRepo.findById(tokenRequest.userId());

        if (byToken.isPresent()) {
            AppToken appToken = byToken.get();
            appToken.setLastUpdate(LocalDateTime.now());
            appToken.setToken(tokenRequest.token());
            appToken.setTokenType(tokenRequest.tokenType() != Platform.APP ? Platform.DASHBOARD : Platform.APP);
            return tokenRepo.save(appToken).getUserId();
        } else {
            AppToken appToken = new AppToken();
            appToken.setToken(tokenRequest.token());
            appToken.setUserId(tokenRequest.userId());
            appToken.setTokenType(tokenRequest.tokenType() != Platform.APP ? Platform.DASHBOARD : Platform.APP);
            return tokenRepo.save(appToken).getUserId();
        }
    }

}
