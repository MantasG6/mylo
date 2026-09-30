package io.github.mantasg6.mylo.domain.widget;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
 * Registry containing all concrete Widget Content Handlers.
 *
 */
@Component
public class WidgetContentHandlerRegistry {

    private final Map<WidgetType, WidgetContentHandler<?>> handlers;

    public WidgetContentHandlerRegistry(List<WidgetContentHandler<?>> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toUnmodifiableMap(WidgetContentHandler::getType, Function.identity()));
    }

    /**
     * Get a concrete Widget Content Handler based on Widget Type.
     *
     * @param widgetType Widget Type of the Widget Content Handler to retrieve.
     * @return Concrete Widget Content Handler.
     */
    public WidgetContentHandler<?> getHandler(WidgetType widgetType) {
        return handlers.get(widgetType);
    }
}
