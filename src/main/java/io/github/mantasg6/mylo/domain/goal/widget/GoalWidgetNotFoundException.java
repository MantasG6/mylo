package io.github.mantasg6.mylo.domain.goal.widget;

/**
 * Exception thrown when Goal Widget for a specified Widget ID is not found.
 *
 */
public class GoalWidgetNotFoundException extends RuntimeException {
    public GoalWidgetNotFoundException(Long widgetId) {
        super(String.format("Widget with ID %d is not a Goal Widget", widgetId));
    }
}
