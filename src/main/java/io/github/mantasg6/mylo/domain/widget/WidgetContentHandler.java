package io.github.mantasg6.mylo.domain.widget;

/**
 * Handles content management for different types of widgets.
 *
 */
public interface WidgetContentHandler<TRequest, TResponse> {
    /**
     * Returns the supported Widget type.
     */
    WidgetType getType();
    /**
     * Builds and returns the Widget content.
     */
    TResponse loadContent();
    /**
     * Creates a Widget with details specified in the request.
     *
     * @param request Details of the Widget to be created.
     */
    TResponse createContent(TRequest request);
}
