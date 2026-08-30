package io.github.mantasg6.mylo.core.exception;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * An exception handler to deal with all the application exceptions.
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String INVALID_VALUE = "Invalid value";
    private static final String VALIDATION_FAILED = "Validation failed";

    /**
     * A generic exception handler for all entity not found exceptions.
     *
     * @param ex Exception to be handled.
     * @return A standard problem details response with a 404 result code.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ValidationProblemDetail problemDetail = new ValidationProblemDetail(HttpStatus.BAD_REQUEST.value());
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problemDetail.setDetail(VALIDATION_FAILED);

        Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        error -> (error instanceof FieldError fieldError) ? fieldError.getField() : error.getObjectName(),
                        error -> Optional.ofNullable(error.getDefaultMessage())
                                .orElse(INVALID_VALUE)
                ));
        problemDetail.setErrors(errors);
        return problemDetail;
    }
}
