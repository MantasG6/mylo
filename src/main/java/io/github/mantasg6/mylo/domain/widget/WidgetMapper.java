package io.github.mantasg6.mylo.domain.widget;

/**
 * Widget Mapper to convert Widget DTO to Entity and vice versa.
 *
 */
public interface WidgetMapper<T> {

    /**
     * Convert Widget entity to Widget Response.
     *
     * @param entity Widget entity to convert.
     */
    WidgetResponse<T> toDto(Widget entity, T content);

    /**
     * Convert Widget Request to Widget entity.
     *
     * @param request Widget Request to convert.
     */
    Widget toEntity(WidgetRequest request);
}
