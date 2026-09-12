package io.github.mantasg6.mylo.domain.widget;

import io.github.mantasg6.mylo.core.exception.EntityNotFoundException;

/**
 * Exception thrown when requested Widget is not found.
 *
 */
public class WidgetNotFoundException extends EntityNotFoundException {

    public WidgetNotFoundException(Long id) {
        super(WidgetErrorMessage.WIDGET_NOT_FOUND(id));
    }
}
