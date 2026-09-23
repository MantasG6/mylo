package io.github.mantasg6.mylo.domain.widget;

/**
 * Widget Mapper to convert Widget DTO to Entity and vice versa.
 *
 */
public interface WidgetContentMapper<T> {

    /**
     * Convert Widget entity to Widget Response.
     *
     * @param entity Widget entity to convert.
     */
    WidgetResponse<T> toDto(Widget entity, T content);
}
