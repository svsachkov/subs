package subs.api.user;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthorizedUser {

    public static String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal instanceof String ? (String) principal : null;
    }
}
