package io.github.mantasg6.mylo.domain.widget;

import io.github.mantasg6.mylo.core.exception.ResourceConflictException;

/**
 * Exception thrown when Widget position is already taken.
 *
 */
public class WidgetPositionException extends ResourceConflictException {

    public WidgetPositionException(Long workspaceId, Integer position) {
        super(WidgetErrorMessage.POSITION_TAKEN(workspaceId, position));
    }
}
