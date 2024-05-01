package ua.mykola.UserRESTfulAPI.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.mykola.UserRESTfulAPI.rest.response.ErrorMessage;
import ua.mykola.UserRESTfulAPI.exception.NotFoundException;
import ua.mykola.UserRESTfulAPI.exception.UnderagePersonException;
import ua.mykola.UserRESTfulAPI.exception.ValidationException;

/**
 * Controller of exceptions.
 */
@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(value = UnderagePersonException.class)
    public ResponseEntity<ErrorMessage> underagePersonException(UnderagePersonException ex) {
        return ResponseEntity
                .badRequest()
                .body(ErrorMessage.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ErrorMessage> validationException(ValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build());
    }
}
