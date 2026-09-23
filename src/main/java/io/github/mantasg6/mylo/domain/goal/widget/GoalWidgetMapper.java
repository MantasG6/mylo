package io.github.mantasg6.mylo.domain.goal.widget;

import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import io.github.mantasg6.mylo.domain.goal.GoalResponse;
import io.github.mantasg6.mylo.domain.widget.Widget;
import io.github.mantasg6.mylo.domain.widget.WidgetContentMapper;
import io.github.mantasg6.mylo.domain.widget.WidgetResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class GoalWidgetMapper implements WidgetContentMapper<GoalResponse> {

	@Override
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "workspaceId", source = "entity.workspace.id")
    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "createdAt", source = "entity.createdAt")
    @Mapping(target = "updatedAt", source = "entity.updatedAt")
    @Mapping(target = "content", expression = "java(goal)")
	public abstract WidgetResponse<GoalResponse> toDto(Widget entity, GoalResponse goal);
}
