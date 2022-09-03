package subs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class ApiException {

    private final String message;           // error message
    private final HttpStatus httpStatus;    // request response status
    private final ZonedDateTime timestamp;  // the time the error occurred
}
