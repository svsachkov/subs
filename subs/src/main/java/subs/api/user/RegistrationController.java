package subs.api.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import subs.exception.ApiRequestException;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
//@RestController
@RequestMapping(path = "/registration")
@AllArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("")
    public String index() {
        return "login";
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRequest request) {
        try {
            userService.register(request);
        } catch (ApiRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User successfully registered", HttpStatus.CREATED);
    }
}
