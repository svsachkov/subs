package subs.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User authentication request by username and password.
 */
@Getter             // creates default getters automatically for each class field
@Setter             // creates default setters automatically for each class field
@NoArgsConstructor  // creates a constructor with no parameters
public class UsernameAndPasswordAuthenticationRequest {

    private String username;
    private String password;
}
