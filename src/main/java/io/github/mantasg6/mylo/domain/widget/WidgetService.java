package io.github.mantasg6.mylo.domain.widget;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.mantasg6.mylo.domain.workspace.Workspace;
import io.github.mantasg6.mylo.domain.workspace.WorkspaceNotFoundException;
import io.github.mantasg6.mylo.domain.workspace.WorkspaceRepository;
import lombok.RequiredArgsConstructor;

/**
 * Widget management service.
 *
 */
@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WorkspaceRepository workspaceRepository;

    private final WidgetRepository widgetRepository;

    private final WidgetRequestMapper widgetRequestMapper;

    private final WidgetContentHandlerRegistry handlerRegistry;


    /**
     * Retrieve all Widgets.
     *
     * @return List of Widgets.
     */
    public List<WidgetResponse<?>> getAllWidgets() {
        List<WidgetResponse<?>> result = new ArrayList<>();
        for (WidgetType type : WidgetType.values()) {
            WidgetContentHandler<?> handler = handlerRegistry.getHandler(type);
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
        WidgetContentHandler<?> handler = handlerRegistry.getHandler(widget.getType());
        return handler.loadContent(widget);
    }

    /**
     * Create a new Widget.
     *
     * @param request Request with details of the new Widget.
     * @return Response with details about the new Widget.
     */
    public WidgetResponse<?> createWidget(WidgetRequest request) {
        Workspace workspace = workspaceRepository.findById(request.workspaceId())
                .orElseThrow(() -> new WorkspaceNotFoundException(request.workspaceId()));
        Widget widget = widgetRequestMapper.toEntity(request);

        workspace.addWidget(widget);

        Widget created = widgetRepository.save(widget);

        WidgetContentHandler<?> handler = handlerRegistry.getHandler(request.type());

        return handler.createContent(created, request.contentId());
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
