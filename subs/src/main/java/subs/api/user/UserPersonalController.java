package subs.api.user;

import subs.exception.ApiRequestException;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/me")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@AllArgsConstructor
public class UserPersonalController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<User> read() {
        try {
            String username = AuthorizedUser.getUsername();
            if (username == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            final User user = (User) userService.loadUserByUsername(username);

            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody UserRequest request) {
        try {
            String username = AuthorizedUser.getUsername();
            if (username == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            final User user = (User) userService.loadUserByUsername(username);

            try {
                final boolean updated = userService.update(user.getId(), request);

                return updated
                        ? new ResponseEntity<>(HttpStatus.OK)
                        : new ResponseEntity<>("You couldn't be found", HttpStatus.NOT_FOUND);
            } catch (ApiRequestException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_MODIFIED);
            }
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> delete() {
        try {
            String username = AuthorizedUser.getUsername();
            if (username == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            final User user = (User) userService.loadUserByUsername(username);

            final boolean deleted = userService.delete(user.getId());

            return deleted
                    ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
