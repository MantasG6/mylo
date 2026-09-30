package io.github.mantasg6.mylo.domain.widget;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // TODO: return widgets with empty content too.
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
        // TODO: Load content of empty widgets.
        return handler.loadContent(widget);
    }

    /**
     * Create a new Widget.
     *
     * @param request Request with details of the new Widget.
     * @return Response with details about the new Widget.
     */
    @Transactional
    public WidgetResponse<?> createWidget(WidgetCreateRequest request) {
        Workspace workspace = workspaceRepository.findById(request.workspaceId())
                .orElseThrow(() -> new WorkspaceNotFoundException(request.workspaceId()));
        Widget widget = widgetRequestMapper.toEntity(request);

        if (positionTaken(workspace, request.position())) {
            throw new WidgetPositionException(workspace.getId(), request.position());
        }

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
    public WidgetResponse<?> updateWidget(Long id, WidgetUpdateRequest request) {
        // Retrieve required entities
        Widget widget = widgetRepository.findById(id)
                .orElseThrow(() -> new WidgetNotFoundException(id));
        Long workspaceId = widget.getWorkspace().getId();
        Workspace widgetWorkspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        // Terminate if position already taken
        if (positionTaken(widgetWorkspace, request.position())) {
            throw new WidgetPositionException(widgetWorkspace.getId(), request.position());
        }

        // Update position
        widget.setPosition(request.position());
        Widget updated = widgetRepository.save(widget);

        // Return full details of the updated widget
        return handlerRegistry.getHandler(updated.getType()).loadContent(updated);
    }

    /**
     * Delete Widget.
     *
     * @param id ID of the Widget to delete.
     */
    public void deleteWidget(Long id) {
        widgetRepository.deleteById(id);
    }

    /**
     * Checks if Widget position is already taken in the Workspace.
     *
     * @param workspace Workspace to check.
     * @param position Position to check.
     * @return true if position is taken, false otherwise.
     */
    private boolean positionTaken(Workspace workspace, int position) {
        return workspace.getWidgets().stream()
                .anyMatch(w -> w.getPosition() == position);
    }
}
