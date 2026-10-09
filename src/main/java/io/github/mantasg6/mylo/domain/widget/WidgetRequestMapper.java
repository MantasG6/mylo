package io.github.mantasg6.mylo.domain.widget;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper for the Widget requests.
 *
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WidgetRequestMapper {

    /**
     * Convert Widget Request DTO to Entity.
     *
     * @param request Request to convert.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    Widget toEntity(WidgetRequest request);
}
