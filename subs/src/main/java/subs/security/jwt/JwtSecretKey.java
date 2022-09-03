package subs.security.jwt;

import io.jsonwebtoken.security.Keys;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@AllArgsConstructor // creates a constructor with 1 parameter for each class field
public class JwtSecretKey {

    private final JwtConfig jwtConfig;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }
}
