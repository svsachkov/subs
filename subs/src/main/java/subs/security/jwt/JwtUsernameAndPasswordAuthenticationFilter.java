package subs.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.time.LocalDate;

/**
 * A filter that receives and checks user data (login, password).
 * Creates a token and sends it to the user.
 */
@AllArgsConstructor // creates a constructor with 1 parameter for each class field
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    /**
     * Performs actual authentication (send credentials -> validates credentials).
     *
     * @param request  user request
     * @param response response
     * @throws AuthenticationException authentication error
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // A query that verifies that the username and password are correct.
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),    // principal
                    authenticationRequest.getPassword()     // credentials
            );

            // AuthenticationManager will check if the username exists and then check if the password is correct.
            // If everything is in order, the request will be authenticated.
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a token and sends it to the user (only called when the attemptAuthentication method is successful).
     *
     * @param request    request
     * @param response   response
     * @param chain      filter call chain
     * @param authResult authentication result
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        // Create a token.
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();

        // Sending a token to a user.
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
        response.addHeader(
                "Role",
                authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null)
        );
    }
}
