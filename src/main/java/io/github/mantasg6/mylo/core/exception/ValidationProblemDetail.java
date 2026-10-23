package io.github.mantasg6.mylo.core.exception;

import java.util.Map;

import org.springframework.http.ProblemDetail;

/**
 * Problem Detail class that also contains validation error map.
 *
 */
public class ValidationProblemDetail extends ProblemDetail {
    private Map<String, String> errors;

    public ValidationProblemDetail() {
        super();
    }

    public ValidationProblemDetail(int rawStatusCode) {
        super(rawStatusCode);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
