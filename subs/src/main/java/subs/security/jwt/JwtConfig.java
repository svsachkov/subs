package subs.security.jwt;

import com.google.common.net.HttpHeaders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter             // creates default getters automatically for each field
@Setter             // creates default setters automatically for each field
@NoArgsConstructor  // creates a constructor with no parameters
@Component
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
