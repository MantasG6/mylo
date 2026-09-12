package io.github.mantasg6.mylo.domain.goal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper to convert Goal DTO to Entity and vice versa.
 *
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GoalMapper {

    /**
     * Converts Goal Entity to Goal Response DTO.
     *
     * @param goal Goal Entity to convert.
     */
    GoalResponse toDto(Goal goal);

    /**
     * Converts Goal Request DTO to Goal Entity.
     *
     * @param request Goal Request DTO to convert.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "progress", ignore = true)
    Goal toEntity(GoalRequest request);
}
