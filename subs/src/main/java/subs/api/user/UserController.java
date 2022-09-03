package subs.api.user;

import subs.api.file.File;
import subs.exception.ApiRequestException;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody User user) {
        try {
            userService.create(user);
        } catch (ApiRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User successfully created", HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<User>> read() {
        final List<User> users = userService.readAll();

        return (users != null && !users.isEmpty()) ? new ResponseEntity<>(users, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> read(@PathVariable(name = "userId") Integer userId) {
        final User user = userService.read(userId);

        return user != null ? new ResponseEntity<>(user, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable(name = "userId") Integer userId, @RequestBody User user) {
        try {
            return userService.update(userId, user) ? new ResponseEntity<>("User updated successfully", HttpStatus.OK) : new ResponseEntity<>("Failed to find user", HttpStatus.NOT_FOUND);
        } catch (ApiRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable(name = "userId") Integer userId) {
        return userService.delete(userId) ? new ResponseEntity<>("User deleted successfully", HttpStatus.OK) : new ResponseEntity<>("Failed to find user", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userId}/files")
    public ResponseEntity<List<File>> getComments(@PathVariable(name = "userId") Integer userId) {
        final User user = userService.read(userId);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final List<File> files = user.getFiles();

        return (files != null && !files.isEmpty()) ? new ResponseEntity<>(files, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
