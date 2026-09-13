package io.github.mantasg6.mylo.domain.widget;

import io.github.mantasg6.mylo.core.exception.CommonApplicationException;

public class WidgetPositionException extends CommonApplicationException {

    public WidgetPositionException(Long workspaceId, Integer position) {
        super(WidgetErrorMessage.POSITION_TAKEN(workspaceId, position));
    }
}
