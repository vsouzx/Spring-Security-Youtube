package br.com.souza.spring_security_essentials.config;

import br.com.souza.spring_security_essentials.database.model.Users;
import br.com.souza.spring_security_essentials.dto.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final int UNAUTHORIZED = 401;
    private final ObjectMapper objectMapper;
    @Value("${jwt.key}")
    private String jwtKey;
    @Value("${jwt.expiration-time}")
    private Integer expirationTime;

    public TokenResponse generateToken(Authentication authentication) {
        final Date now = new Date();
        long expirationInMillis = expirationTime.longValue();
        Date expirationDate = new Date(System.currentTimeMillis() + expirationInMillis);

        final Users user = getUsuario(authentication);

        final String auth = Jwts.builder()
                .setIssuer("WEB Token") 
                .setSubject(user.toString())
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, jwtKey.getBytes(StandardCharsets.UTF_8))
                .compact();

        return TokenResponse.builder()
                .token(auth)
                .expiresIn(expirationInMillis)
                .username(user.getUsername())
                .build();
    }

    public boolean isValid(String jwt, ServletResponse response) throws IOException {
        try {
            jwt = extractToken(jwt);
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            JWSVerifier verifier = new MACVerifier(jwtKey.getBytes(StandardCharsets.UTF_8));
            if (!signedJWT.verify(verifier)) {
                log.error("Assinatura inválida!");
                ((HttpServletResponse) response).sendError(UNAUTHORIZED);
                return false;
            }
            Jwts.parser().setSigningKey(jwtKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(jwt);
            return true;
        }catch (Exception e) {
            log.error("Token inválido: {}", e.getMessage());
            ((HttpServletResponse) response).sendError(UNAUTHORIZED);
            return false;
        }
    }

    public Users getUserFromToken(String jwt) throws JsonProcessingException {
        jwt = extractToken(jwt);
        Claims claims = Jwts.parser().setSigningKey(jwtKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(jwt).getBody();
        return objectMapper.readValue(claims.getSubject(), Users.class);
    }

    public Users getUsuario(Authentication authentication) {
        return (Users) authentication.getPrincipal();
    }

    private String extractToken(String authToken) {
        if (authToken.toLowerCase().startsWith("bearer")) {
            return authToken.substring("bearer ".length());
        }
        return authToken;
    }
}