package io.github.mantasg6.mylo.core.exception;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * An exception handler to deal with all the application exceptions.
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String INVALID_VALUE = "Invalid value";
    private static final String VALIDATION_FAILED = "Validation failed";

    /**
     * Catch unexpected exceptions.
     *
     * @param ex Any exception that has not been caught yet.
     * @param request Request details.
     * @return Problem Detail containing a clean result for user.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception on {} {}", request.getMethod(), request.getRequestURI(), ex);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problem.setInstance(URI.create(request.getRequestURI()));

        return problem;
    }

    /**
     * Generic exception handler for all entity not found exceptions.
     *
     * @param ex Exception to be handled.
     * @return Standard problem details response with a 404 result code.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Exception handler to handle the validation errors in the application requests.
     *
     * @param ex Validation exception that is handled.
     * @return Problem details response extended with the validation error map and a result code of 400.
     */
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

    /**
     * Exception handler to handle all the common application failures.
     *
     * @param ex Custom exception that represents a common application failure.
     * @return Response containing problem details and 400 result code.
     */
    @ExceptionHandler(CommonApplicationException.class)
    public ProblemDetail handleCommonFailures(CommonApplicationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
