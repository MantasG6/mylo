package io.github.mantasg6.mylo.domain.widget;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Widget Mapper to convert Widget DTO to Entity and vice versa.
 *
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WidgetMapper {

    /**
     * Convert Widget entity to Widget Response.
     *
     * @param entity Widget entity to convert.
     */
    @Mapping(target = "workspaceId", source = "workspace.id")
    WidgetResponse toDto(Widget entity);


    /**
     * Convert Widget Request to Widget entity.
     *
     * @param request Widget Request to convert.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    Widget toEntity(WidgetRequest request);
}
