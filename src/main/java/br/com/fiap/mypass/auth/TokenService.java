package br.com.fiap.mypass.auth;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    String generateToken(String username){
        Instant now = Instant.now();

        JwtClaimsSet param = JwtClaimsSet.builder()
                .subject(username)
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                .claim("role", "ADMIN")
                .build();

        JwtEncoderParameters jwtClaimsSet = JwtEncoderParameters.from(param);
        return jwtEncoder.encode(jwtClaimsSet).getTokenValue();
    }
}
