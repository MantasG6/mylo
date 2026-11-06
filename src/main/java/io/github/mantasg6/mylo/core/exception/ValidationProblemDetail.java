package io.github.mantasg6.mylo.core.exception;

import java.util.List;
import java.util.Map;

import org.springframework.http.ProblemDetail;

/**
 * Problem Detail class that also contains validation error map.
 *
 */
public class ValidationProblemDetail extends ProblemDetail {
    private Map<String, List<String>> errors;

    public ValidationProblemDetail() {
        super();
    }

    public ValidationProblemDetail(int rawStatusCode) {
        super(rawStatusCode);
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }
}
