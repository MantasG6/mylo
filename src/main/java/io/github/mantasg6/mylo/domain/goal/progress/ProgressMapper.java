package io.github.mantasg6.mylo.domain.goal.progress;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper to convert Progress DTO to Entity and vice versa.
 *
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProgressMapper {

    /**
     * Converts Progress Entity to Progress Response DTO.
     *
     * @param progress Progress Entity to convert.
     */
    ProgressResponse toDto(Progress progress);

    /**
     * Converts Progress Request DTO to Progress Entity.
     *
     * @param request Progress Request to convert.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "goal", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Progress toEntity(ProgressRequest request);
}
