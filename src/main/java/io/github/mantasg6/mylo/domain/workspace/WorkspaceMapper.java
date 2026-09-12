package io.github.mantasg6.mylo.domain.workspace;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import io.github.mantasg6.mylo.domain.widget.WidgetMapper;

/**
 * Mapper to convert Workspace DTO to Entity and vice versa.
 *
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = WidgetMapper.class
)
public interface WorkspaceMapper {

    /**
     * Converts Workspace entity to WorkspaceResponse DTO.
     *
     * @param entity Entity to be converted.
     */
    WorkspaceResponse toDto(Workspace entity);

    /**
     * Converts WorkspaceRequest DTO to Workspace entity.
     *
     * @param dto WorkspaceRequest to be converted.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "widgets", ignore = true)
    Workspace toEntity(WorkspaceRequest dto);
}
