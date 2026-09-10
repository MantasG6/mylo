package io.github.mantasg6.mylo.domain.workspace;

import io.github.mantasg6.mylo.core.exception.CommonApplicationException;

/**
 * Exception thrown when there's a system error or restriction regarding workspace period.
 *
 */
public class WorkspacePeriodException extends CommonApplicationException {

    public WorkspacePeriodException(String message) {
        super(message);
    }
}
