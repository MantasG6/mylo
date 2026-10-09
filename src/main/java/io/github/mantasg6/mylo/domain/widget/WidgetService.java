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
        List<Widget> allWidgets = widgetRepository.findAll();
        List<WidgetResponse<?>> result = new ArrayList<>();

        for (WidgetType type : WidgetType.values()) {
            WidgetContentHandler<?> handler = handlerRegistry.getHandler(type);
            List<Widget> typeWidgets = allWidgets.stream()
                    .filter(w -> w.getType().equals(type))
                    .toList();

            result.addAll(handler.mapContentList(typeWidgets));
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
    @Transactional
    public WidgetResponse<?> createWidget(WidgetRequest request) {
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
    public WidgetResponse<?> updateWidget(Long id, WidgetRequest request) {
        // Retrieve required entities.
        Widget widget = widgetRepository.findById(id)
                .orElseThrow(() -> new WidgetNotFoundException(id));
        Long workspaceId = widget.getWorkspace().getId();
        Workspace widgetWorkspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        // Update type. TODO: Implement type update when more than 1 type is implemented.
        if (request.type() != null) {
            throw new UnsupportedOperationException("Type update is not implemented yet");
        }

        // Update Workspace.
        if (request.workspaceId() != null) {
            Workspace newWorkspace = workspaceRepository.findById(request.workspaceId())
                    .orElseThrow(() -> new WorkspaceNotFoundException(request.workspaceId()));
            widgetWorkspace.transferWidget(widget, newWorkspace);
            widgetWorkspace = newWorkspace;
        }

        // Update position.
        if (request.position() != null) {
            // Terminate if position already taken.
            if (positionTaken(widgetWorkspace, request.position())) {
                throw new WidgetPositionException(widgetWorkspace.getId(), request.position());
            }
            widget.setPosition(request.position());
        }

        // Persist updated Widget.
        Widget updated = widgetRepository.save(widget);

        // Update Widget content and return full details of the updated Widget.
        if (request.contentId() != null) {
            return handlerRegistry.getHandler(updated.getType()).updateContent(updated, request.contentId());
        }
        
        // No content updates, return new Widget with unchanged content.
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
     * Helper to check if Widget position is already taken in the Workspace.
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
