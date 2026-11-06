package io.github.mantasg6.mylo.domain.widget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mantasg6.mylo.domain.goal.GoalResponse;
import io.github.mantasg6.mylo.domain.goal.widget.GoalWidgetContentHandler;
import io.github.mantasg6.mylo.domain.goal.widget.GoalWidgetMapper;
import io.github.mantasg6.mylo.domain.workspace.Workspace;
import io.github.mantasg6.mylo.domain.workspace.WorkspaceNotFoundException;
import io.github.mantasg6.mylo.domain.workspace.WorkspaceRepository;

@ExtendWith(MockitoExtension.class)
public class WidgetServiceTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private WidgetRepository widgetRepository;

    @Mock
    private GoalWidgetContentHandler goalWidgetContentHandler;

    private WidgetContentMapper<GoalResponse> widgetContentMapper;

    private WidgetService widgetService;

    @BeforeEach
    void setUp() {
        widgetContentMapper = Mappers.getMapper(GoalWidgetMapper.class);
        WidgetRequestMapper widgetRequestMapper = Mappers.getMapper(WidgetRequestMapper.class);

        WidgetContentHandlerRegistry handlerRegistry = mock(WidgetContentHandlerRegistry.class);
        lenient().doReturn(goalWidgetContentHandler).when(handlerRegistry).getHandler(WidgetType.GOAL);

        widgetService = new WidgetService(workspaceRepository, widgetRepository,
                widgetRequestMapper, handlerRegistry);
    }

    @Test
    void getWidgetById_shouldReturnWidget_whenValidId() {
        Workspace workspace = new Workspace();
        Widget widget = new Widget(workspace, 1, WidgetType.GOAL);
        widget.setId(1L);
        when(widgetRepository.findById(1L)).thenReturn(Optional.of(widget));
        when(goalWidgetContentHandler.loadContent(widget)).thenReturn(widgetContentMapper.toDto(widget, null));

        WidgetResponse<?> actual = widgetService.getWidgetById(1L);

        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.position()).isEqualTo(1);
        assertThat(actual.type()).isEqualTo(WidgetType.GOAL);
        assertThat(actual.content()).isNull();
    }

    @Test
    void getWidgetById_shouldThrow_whenWidgetNotFound() {
        when(widgetRepository.findById(99L)).thenReturn(Optional.empty());

        WidgetNotFoundException actual = assertThrows(
                WidgetNotFoundException.class,
                () -> widgetService.getWidgetById(99L)
        );

        assertThat(actual.getMessage()).isEqualTo("Widget with id 99 not found!");
    }

    @Test
    void createWidget_shouldThrow_whenWorkspaceNotFound() {
        WidgetRequest request = WidgetRequest.builder()
                .workspaceId(11L).build();
        when(workspaceRepository.findById(11L)).thenReturn(Optional.empty());

        WorkspaceNotFoundException actual = assertThrows(
                WorkspaceNotFoundException.class,
                () -> widgetService.createWidget(request)
        );

        assertThat(actual.getMessage()).isEqualTo("Workspace with id 11 not found!");
    }

    @Test
    void createWidget_shouldThrow_whenPositionTaken() {
        WidgetRequest request = WidgetRequest.builder()
                .workspaceId(11L).position(1).build();
        Workspace workspace = new Workspace();
        workspace.setId(11L);
        Widget other = new Widget();
        other.setPosition(1);
        workspace.addWidget(other);
        when(workspaceRepository.findById(11L)).thenReturn(Optional.of(workspace));

        WidgetPositionException actual = assertThrows(
                WidgetPositionException.class,
                () -> widgetService.createWidget(request)
        );

        assertThat(actual.getMessage()).isEqualTo("Position 1 is already taken in Workspace with id 11.");
    }

    @Test
    void createWidget_shouldReturnCreatedWidget_whenValidRequest() {
        WidgetRequest request = WidgetRequest.builder()
                .workspaceId(11L)
                .position(1)
                .type(WidgetType.GOAL)
                .contentId(1L)
                .build();
        Workspace workspace = new Workspace();
        workspace.setId(11L);
        when(workspaceRepository.findById(11L)).thenReturn(Optional.of(workspace));
        Widget created = new Widget(workspace, 1, WidgetType.GOAL);
        created.setId(1L);
        when(widgetRepository.save(any(Widget.class))).thenReturn(created);
        when(goalWidgetContentHandler.createContent(created, 1L))
                .thenReturn(widgetContentMapper.toDto(created, null));

        WidgetResponse<?> actual = widgetService.createWidget(request);

        verify(widgetRepository).save(any(Widget.class));
        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.workspaceId()).isEqualTo(11L);
        assertThat(actual.position()).isEqualTo(1);
        assertThat(actual.type()).isEqualTo(WidgetType.GOAL);
    }

    @Test
    void updateWidget_shouldThrow_whenWidgetNotFound() {
        when(widgetRepository.findById(99L)).thenReturn(Optional.empty());

        WidgetNotFoundException actual = assertThrows(
                WidgetNotFoundException.class,
                () -> widgetService.updateWidget(99L, WidgetRequest.builder().build())
        );

        assertThat(actual.getMessage()).isEqualTo("Widget with id 99 not found!");
    }

    @Test
    void updateWidget_shouldThrow_whenCurrentWorkspaceNotFound() {
        Workspace workspace = new Workspace();
        workspace.setId(99L);
        Widget widget = new Widget();
        widget.setId(1L);
        workspace.addWidget(widget);
        when(widgetRepository.findById(1L)).thenReturn(Optional.of(widget));
        when(workspaceRepository.findById(99L)).thenReturn(Optional.empty());

        WorkspaceNotFoundException actual = assertThrows(
                WorkspaceNotFoundException.class,
                () -> widgetService.updateWidget(1L, WidgetRequest.builder().build())
        );

        assertThat(actual.getMessage()).isEqualTo("Workspace with id 99 not found!");
    }

    @Test
    void updateWidget_shouldThrow_whenTypeInRequest() {
        WidgetRequest request = WidgetRequest.builder()
                .type(WidgetType.GOAL).build();
        Workspace workspace = new Workspace();
        workspace.setId(11L);
        Widget widget = new Widget();
        widget.setId(1L);
        workspace.addWidget(widget);
        when(widgetRepository.findById(1L)).thenReturn(Optional.of(widget));
        when(workspaceRepository.findById(11L)).thenReturn(Optional.of(workspace));

        UnsupportedOperationException actual = assertThrows(
            UnsupportedOperationException.class,
            () -> widgetService.updateWidget(1L, request)
        );

        assertThat(actual.getMessage()).isEqualTo("Type update is not implemented yet");
    }

    @Test
    void updateWidget_shouldThrow_whenNewWorkspaceNotFound() {
        WidgetRequest request = WidgetRequest.builder()
                .workspaceId(99L).build();
        Workspace workspace = new Workspace();
        workspace.setId(11L);
        Widget widget = new Widget();
        widget.setId(1L);
        workspace.addWidget(widget);
        when(widgetRepository.findById(1L)).thenReturn(Optional.of(widget));
        when(workspaceRepository.findById(11L)).thenReturn(Optional.of(workspace));
        when(workspaceRepository.findById(99L)).thenReturn(Optional.empty());

        WorkspaceNotFoundException actual = assertThrows(
                WorkspaceNotFoundException.class,
                () -> widgetService.updateWidget(1L, request)
        );

        assertThat(actual.getMessage()).isEqualTo("Workspace with id 99 not found!");
    }

    @Test
    void updateWidget_shouldThrow_whenPositionTaken() {
        WidgetRequest request = WidgetRequest.builder()
                .position(1).build();
        Workspace workspace = new Workspace();
        workspace.setId(22L);
        Widget other = new Widget();
        other.setPosition(1);
        workspace.addWidget(other);
        Widget widget = new Widget();
        widget.setId(1L);
        workspace.addWidget(widget);
        when(widgetRepository.findById(1L)).thenReturn(Optional.of(widget));
        when(workspaceRepository.findById(22L)).thenReturn(Optional.of(workspace));

        WidgetPositionException actual = assertThrows(
                WidgetPositionException.class,
                () -> widgetService.updateWidget(1L, request)
        );

        assertThat(actual.getMessage()).isEqualTo("Position 1 is already taken in Workspace with id 22.");
    }

    @Test
    void updateWidget_shouldReturnUpdatedWidget_whenValidRequest() {
        WidgetRequest request = WidgetRequest.builder()
                .workspaceId(22L)
                .position(2)
                .build();
        Workspace currentWorkspace = new Workspace();
        currentWorkspace.setId(11L);
        Widget currentWidget = new Widget(currentWorkspace, 1, WidgetType.GOAL);
        currentWorkspace.addWidget(currentWidget);
        when(widgetRepository.findById(1L)).thenReturn(Optional.of(currentWidget));
        when(workspaceRepository.findById(11L)).thenReturn(Optional.of(currentWorkspace));
        Workspace newWorkspace = new Workspace();
        newWorkspace.setId(22L);
        when(workspaceRepository.findById(22L)).thenReturn(Optional.of(newWorkspace));
        Widget updated = new Widget(newWorkspace, 2, WidgetType.GOAL);
        updated.setId(1L);
        when(widgetRepository.save(any(Widget.class))).thenReturn(updated);
        when(goalWidgetContentHandler.loadContent(updated))
                .thenReturn(widgetContentMapper.toDto(updated, null));

        WidgetResponse<?> actual = widgetService.updateWidget(1L, request);

        verify(widgetRepository).save(any(Widget.class));
        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.workspaceId()).isEqualTo(22L);
        assertThat(actual.position()).isEqualTo(2);
        assertThat(actual.type()).isEqualTo(WidgetType.GOAL);
    }

}
