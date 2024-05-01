package ua.mykola.UserRESTfulAPI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
/**
 * An exception to indicate that an operation
 * involving an underage person is not permitted.
 */
public class UnderagePersonException extends RuntimeException{

    public UnderagePersonException(String message) {
        super(message);
    }
}
