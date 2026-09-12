package io.github.mantasg6.mylo.domain.widget;

/**
 * Class containing all Widget error messages.
 *
 */
public class WidgetErrorMessage {

    /**
     * Widget not found message.
     *
     * @param id ID that was not found.
     * @return Formatted message for the user.
     */
    public static final String WIDGET_NOT_FOUND(Long id) {
        return String.format("Widget with id %d not found!", id);
    }
}
