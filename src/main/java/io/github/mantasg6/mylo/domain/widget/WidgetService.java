package io.github.mantasg6.mylo.domain.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Widget management service.
 *
 */
@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WidgetRepository widgetRepository;

    private final Map<WidgetType, WidgetContentHandler<?>> handlers;

    public WidgetService(WidgetRepository widgetRepository, List<WidgetContentHandler<?>> handlerList) {
        this.widgetRepository = widgetRepository;
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(WidgetContentHandler::getType, h -> h));
    }

    /**
     * Retrieve all Widgets.
     *
     * @return List of Widgets.
     */
    public List<WidgetResponse<?>> getAllWidgets() {
        List<WidgetResponse<?>> result = new ArrayList<>();
        for (WidgetType type : WidgetType.values()) {
            WidgetContentHandler<?> handler = handlers.get(type);
            result.addAll(handler.loadAllContent());
        }
        return result;
    }

    /**
     * Retrieve a single Widget.
     *
     * @param id ID of the Widget to retrieve.
     * @return Retrieved Widget details.
     */
    public WidgetResponse<?> getWidgetById(Long id) {
        Widget widget = widgetRepository.findById(id)
                .orElseThrow(() -> new WidgetNotFoundException(id));
        WidgetContentHandler<?> handler = handlers.get(widget.getType());
        return null;
    }

    /**
     * Create a new Widget.
     *
     * @param request Request with details of the new Widget.
     * @return Response with details about the new Widget.
     */
    public WidgetResponse createWidget(WidgetRequest request) {
        // TODO: Implement
        return null;
    }

    /**
     * Update Widget.
     *
     * @param id ID of the Widget to update.
     * @param request Request with Widget details to update.
     * @return Details of the updated Widget.
     */
    public WidgetResponse updateWidget(Long id, WidgetRequest request) {
        // TODO: Implement
        return null;
    }

    /**
     * Delete Widget.
     *
     * @param id ID of the Widget to delete.
     */
    public void deleteWidget(Long id) {
        // TODO: Implement
    }
}
