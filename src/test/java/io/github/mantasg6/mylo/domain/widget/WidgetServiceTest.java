package io.github.mantasg6.mylo.domain.widget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mantasg6.mylo.domain.workspace.Workspace;
import io.github.mantasg6.mylo.domain.workspace.WorkspaceRepository;

@ExtendWith(MockitoExtension.class)
public class WidgetServiceTest {

    @Mock
    private WidgetRepository widgetRepository;

    @Mock
    private WidgetContentMapper widgetContentMapper;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private WidgetService widgetService;

    @Test
    void createWidget_shouldThrowException_whenPositionInWorkspaceTaken() {
        int position = 1;
        Long workspaceId = 1L;
        WidgetRequest request = WidgetRequest.builder()
                .workspaceId(workspaceId)
                .position(position)
                .build();

        Widget alreadyExisting = new Widget();
        alreadyExisting.setPosition(position);
        Widget newWidget = new Widget();
        newWidget.setPosition(position);
        when(widgetContentMapper.toEntity(request)).thenReturn(newWidget);

        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.addWidget(alreadyExisting);
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspace));


        WidgetPositionException actual = assertThrows(
            WidgetPositionException.class,
            () -> widgetService.createWidget(request)
        );

        assertThat(actual.getMessage()).isEqualTo("Position 1 is already taken in Workspace with id 1.");
    }

    @Test
    void updateWidget_shouldThrowException_whenPositionInWorkspaceTaken() {
        int position = 1;
        Long workspaceId = 1L;
        Long widgetId = 2L;
        WidgetRequest request = WidgetRequest.builder().position(position).build();

        Widget widgetToUpdate = new Widget();
        widgetToUpdate.setId(widgetId);
        widgetToUpdate.setPosition(position);
        when(widgetRepository.findById(widgetId)).thenReturn(Optional.of(widgetToUpdate));

        Widget otherWidget = new Widget();
        otherWidget.setPosition(position);

        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.addWidget(otherWidget);
        workspace.addWidget(widgetToUpdate);
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspace));


        WidgetPositionException actual = assertThrows(
            WidgetPositionException.class,
            () -> widgetService.updateWidget(widgetId, request)
        );

        assertThat(actual.getMessage()).isEqualTo("Position 1 is already taken in Workspace with id 1.");
    }
}
