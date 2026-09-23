package io.github.mantasg6.mylo.domain.widget;

import java.util.List;

/**
 * Handles content management for different types of widgets.
 *
 */
public interface WidgetContentHandler<T> {
    /**
     * Returns the supported Widget type.
     */
    WidgetType getType();

    /**
     * Builds and returns the Widget content.
     *
     * @param widget Base widget features.
     */
    List<WidgetResponse<T>> loadAllContent();

    /**
     * Builds and returns details of a single Widget.
     *
     * @param widget The Widget to build and return details for.
     */
    WidgetResponse<T> loadContent(Widget widget);

    /**
     * Creates a Widget with details specified in the request.
     *
     * @param baseRequest Common Widget features.
     * @param request Features specific to the Widget.
     */
    WidgetResponse<T> createContent(Widget widget, Long contentId);
}
