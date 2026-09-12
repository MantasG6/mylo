package io.github.mantasg6.mylo.domain.widget;

import java.util.List;
import java.util.Optional;

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

    private final WidgetRepository widgetRepository;
    private final WidgetMapper widgetMapper;
    private final WorkspaceRepository workspaceRepository;

    /**
     * Retrieve all Widgets.
     *
     * @return List of Widgets.
     */
    public List<WidgetResponse> getAllWidgets() {
        return widgetRepository.findAll().stream()
                .map(widgetMapper::toDto)
                .toList();
    }

    /**
     * Retrieve a single Widget.
     *
     * @param id ID of the Widget to retrieve.
     * @return Retrieved Widget details.
     */
    public WidgetResponse getWidgetById(Long id) {
        Widget widget = widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id));
        return widgetMapper.toDto(widget);
    }

    /**
     * Create a new Widget.
     *
     * @param request Request with details of the new Widget.
     * @return Response with details about the new Widget.
     */
    public WidgetResponse createWidget(WidgetRequest request) {
        Widget widget = widgetMapper.toEntity(request);

        Workspace workspace = workspaceRepository.findById(request.workspaceId())
                .orElseThrow(() -> new WorkspaceNotFoundException(request.workspaceId()));

        workspace.addWidget(widget);

        return widgetMapper.toDto(widgetRepository.save(widget));
    }

    /**
     * Update Widget.
     *
     * @param id ID of the Widget to update.
     * @param request Request with Widget details to update.
     * @return Details of the updated Widget.
     */
    public WidgetResponse updateWidget(Long id, WidgetRequest request) {
        Widget widget = widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id));

        Optional.of(request.position()).ifPresent(widget::setPosition);

        return widgetMapper.toDto(widgetRepository.save(widget));
    }

    /**
     * Delete Widget.
     *
     * @param id ID of the Widget to delete.
     */
    public void deleteWidget(Long id) {
        widgetRepository.deleteById(id);
    }
}
