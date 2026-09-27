package io.github.mantasg6.mylo.domain.workspace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import io.github.mantasg6.mylo.domain.widget.Widget;
import io.github.mantasg6.mylo.domain.widget.WidgetContentHandler;
import io.github.mantasg6.mylo.domain.widget.WidgetContentHandlerRegistry;
import io.github.mantasg6.mylo.domain.widget.WidgetResponse;
import io.github.mantasg6.mylo.domain.widget.WidgetType;

/**
 * Mapper to convert Workspace DTO to Entity and vice versa.
 *
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class WorkspaceMapper {

    // NOTE: MapStruct does not support custom constructors, hence cannot use constructor injection
    @Autowired
    protected WidgetContentHandlerRegistry handlerRegistry;

    /**
     * Converts Workspace entity to WorkspaceResponse DTO.
     *
     * @param entity Entity to be converted.
     */
    @Mapping(target = "widgets", ignore = true)
    public abstract WorkspaceResponse toDto(Workspace entity);

    /**
     * Custom method to map Workspace Widgets.
     *
     * @param entity Workspace entity containing the Widgets.
     * @param builder Workspace Response builder that needs to be populated with details of the Widgets.
     */
    @AfterMapping
    protected void mapWidgets(
            Workspace entity,
            @MappingTarget WorkspaceResponse.WorkspaceResponseBuilder builder
    ) {
        List<WidgetResponse<?>> widgetResponses = new ArrayList<>();
        Map<WidgetType, List<Widget>> widgetsByType = entity.getWidgets().stream()
                .collect(Collectors.groupingBy(Widget::getType));
        for (Map.Entry<WidgetType, List<Widget>> entry : widgetsByType.entrySet()) {
            WidgetContentHandler<?> handler = handlerRegistry.getHandler(entry.getKey());
            widgetResponses.addAll(handler.mapContentList(entry.getValue()));
        }
        builder.widgets(widgetResponses);
    }

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
    public abstract Workspace toEntity(WorkspaceRequest dto);
}
