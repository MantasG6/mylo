package io.github.mantasg6.mylo.core.exception;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.exc.InvalidFormatException;

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
    private static final String INVALID_JSON = "Invalid JSON request";

    /**
     * Handle validation errors.
     *
     * @param ex Exception containing validation errors.
     * @return Problem Detail with validation error map.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ValidationProblemDetail handleUnreadable(HttpMessageNotReadableException ex) {
        ValidationProblemDetail problemDetail = new ValidationProblemDetail(HttpStatus.BAD_REQUEST.value());
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problemDetail.setDetail(INVALID_JSON);

        Map<String, List<String>> errors = new HashMap<>();

        if (ex.getCause() instanceof InvalidFormatException ife && !ife.getPath().isEmpty()) {
            String field = ife.getPath().get(0).getPropertyName();
            String expectedType = ife.getTargetType().getSimpleName();
            errors.put(field, List.of("must be a valid " + expectedType.toLowerCase()));
        }

        problemDetail.setErrors(errors);

        return problemDetail;
    }

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
     * Exception handler to handle the validation errors in the application request body.
     *
     * @param ex Validation exception that is handled.
     * @return Problem details response extended with the validation error map and a result code of 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationProblemDetail handleRequestBodyValidation(MethodArgumentNotValidException ex) {
        ValidationProblemDetail problemDetail = new ValidationProblemDetail(HttpStatus.BAD_REQUEST.value());
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problemDetail.setDetail(VALIDATION_FAILED);

        problemDetail.setErrors(mapFieldErrors(ex.getBindingResult().getAllErrors()));

        return problemDetail;
    }


    /**
     * Exception handler to handle the validation errors in the application path variables.
     *
     * @param ex Validation exception that is handled.
     * @return Problem details response extended with the validation error map and a result code of 400.
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ValidationProblemDetail handlePathVariableValidation(HandlerMethodValidationException ex) {
        ValidationProblemDetail problemDetail = new ValidationProblemDetail(HttpStatus.BAD_REQUEST.value());
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problemDetail.setDetail(VALIDATION_FAILED);

        Map<String, List<String>> errors = new HashMap<>();
        for (ParameterValidationResult result : ex.getParameterValidationResults()) {
            if (result instanceof ParameterErrors pe) {
                errors.putAll(mapFieldErrors(pe.getAllErrors()));
            } else {
                String name = result.getMethodParameter().getParameterName();
                errors.computeIfAbsent(name, k -> new ArrayList<>())
                        .addAll(result.getResolvableErrors().stream()
                                .map(MessageSourceResolvable::getDefaultMessage).toList()
                        );
            }
        }

        problemDetail.setErrors(errors);
        return problemDetail;
    }

    /**
     * Group field errors by field name.
     *
     * @param errors Field error list.
     * @return Map of field errors grouped by field name.
     */
    private static Map<String, List<String>> mapFieldErrors(List<ObjectError> errors) {
        return errors.stream()
                .collect(Collectors.groupingBy(
                            error -> (error instanceof FieldError fieldError) ? fieldError.getField()
                                    : error.getObjectName(),
                            Collectors.mapping(
                                    error -> Optional.ofNullable(error.getDefaultMessage()).orElse(INVALID_VALUE),
                                    Collectors.toList()
                            )
                        )
                );
    }

    /**
     * Exception handler to handle all the resource conflict exceptions.
     *
     * @param ex Custom exception that represents a resource conflict failure.
     * @return Response containing problem details and 409 result code.
     */
    @ExceptionHandler(ResourceConflictException.class)
    public ProblemDetail handleResourceConflict(ResourceConflictException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }
}
