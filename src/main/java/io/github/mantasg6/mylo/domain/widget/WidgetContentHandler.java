package io.github.mantasg6.mylo.domain.widget;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
     * Builds and returns details of a single Widget.
     *
     * @param widget The Widget to build and return details for.
     */
    WidgetResponse<T> loadContent(Widget widget);

    /**
     * Creates a Widget with details specified in the request.
     *
     * @param widget Base Widget entity.
     * @param contentId Id for the Widget content reference.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    WidgetResponse<T> createContent(Widget widget, Long contentId);

    /**
     * Updates a Widget with details specified in the request.
     * Returns Widget Response with unchanged content mapped if provided contentId is the same.
     *
     * @param widget Base Widget entity.
     * @param contentId Id for the new Widget content reference.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    WidgetResponse<T> updateContent(Widget widget, Long contentId);

    /**
     * Maps Widget entity list to Widget Response list.
     *
     * @param widgets List of Widgets to map.
     */
    List<WidgetResponse<T>> mapContentList(List<Widget> widgets);
}
