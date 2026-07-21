package io.github.mantasg6.mylo.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * An exception handler to deal with all the application exceptions.
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * A generic exception handler for all entity not found exceptions.
     *
     * @param ex Exception to be handled.
     * @return A standard problem details response with a 404 result code.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
