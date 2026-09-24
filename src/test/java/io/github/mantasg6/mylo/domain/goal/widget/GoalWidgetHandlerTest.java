package io.github.mantasg6.mylo.domain.goal.widget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mantasg6.mylo.domain.goal.Goal;
import io.github.mantasg6.mylo.domain.goal.GoalMapper;
import io.github.mantasg6.mylo.domain.goal.GoalRepository;
import io.github.mantasg6.mylo.domain.goal.GoalResponse;
import io.github.mantasg6.mylo.domain.widget.Widget;
import io.github.mantasg6.mylo.domain.widget.WidgetContentMapper;
import io.github.mantasg6.mylo.domain.widget.WidgetRepository;
import io.github.mantasg6.mylo.domain.widget.WidgetRequest;
import io.github.mantasg6.mylo.domain.widget.WidgetRequestMapper;
import io.github.mantasg6.mylo.domain.widget.WidgetResponse;
import io.github.mantasg6.mylo.domain.widget.WidgetService;
import io.github.mantasg6.mylo.domain.widget.WidgetType;
import io.github.mantasg6.mylo.domain.workspace.Workspace;
import io.github.mantasg6.mylo.domain.workspace.WorkspaceRepository;

@ExtendWith(MockitoExtension.class)
public class GoalWidgetHandlerTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private WidgetRepository widgetRepository;

    @Mock
    private GoalWidgetRepository goalWidgetRepository;

    @Mock
    private GoalRepository goalRepository;

    private WidgetContentMapper<GoalResponse> widgetContentMapper;

    private WidgetRequestMapper widgetRequestMapper;

    private GoalMapper goalMapper;

    private GoalWidgetContentHandler goalHandler;

    private WidgetService widgetService;

    @BeforeEach
    void setUp() {
        widgetContentMapper = Mappers.getMapper(GoalWidgetMapper.class);
        goalMapper = Mappers.getMapper(GoalMapper.class);
        widgetRequestMapper = Mappers.getMapper(WidgetRequestMapper.class);
        goalHandler = new GoalWidgetContentHandler(
                goalWidgetRepository, widgetContentMapper,
                goalRepository, goalMapper
        );
        widgetService = new WidgetService(
            workspaceRepository,
            widgetRepository,
            widgetRequestMapper,
            List.of(goalHandler)
        );
    }

    @Test
    void createWidget_returnsCreatedWidgetWithGoal_whenGoalTypeInRequest() {
        // Set up the request and empty workspace
        WidgetRequest request = WidgetRequest.builder()
                .workspaceId(1L)
                .type(WidgetType.GOAL)
                .contentId(1L)
                .build();

        Workspace workspace = new Workspace();
        workspace.setId(1L);

        Widget widget = new Widget(workspace, 1, WidgetType.GOAL);
        widget.setId(1L);

        Goal goal = new Goal();
        goal.setId(1L);
        goal.setName("Goal #1");

        GoalWidget goalWidget = new GoalWidget(widget, goal);
        goalWidget.setId(1L);

        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(widgetRepository.save(any())).thenReturn(widget);
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(goalWidgetRepository.save(any())).thenReturn(goalWidget);

        // Execute test
        WidgetResponse<?> actual = widgetService.createWidget(request);

        // Assert expected values
        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.content()).isInstanceOf(GoalResponse.class);
        GoalResponse actualGoal = (GoalResponse) actual.content();
        assertThat(actualGoal.id()).isEqualTo(1L);
        assertThat(actualGoal.name()).isEqualTo("Goal #1");
    }

    @Test
    void getAllWidgets_shouldReturnAllGoalWidgets_whenGoalWidgetContentHandlerIsProvided() {
        // Set up the workspace and everything that is included in it (widget, goal, goalWidget)
        Workspace workspace = new Workspace();
        workspace.setId(1L);

        Widget widget = new Widget(workspace, 1, WidgetType.GOAL);
        widget.setId(1L);

        workspace.addWidget(widget);

        Goal goal = new Goal();
        goal.setId(1L);
        goal.setName("Goal #1");

        GoalWidget goalWidget = new GoalWidget(widget, goal);
        goalWidget.setId(1L);

        when(goalWidgetRepository.findAll()).thenReturn(List.of(goalWidget));

        // Execute the test
        List<WidgetResponse<?>> actual = widgetService.getAllWidgets();

        // Assert expected values
        assertThat(actual.get(0).content()).isInstanceOf(GoalResponse.class);
        GoalResponse actualGoal = (GoalResponse) actual.get(0).content();
        assertThat(actualGoal.id()).isEqualTo(1L);
        assertThat(actualGoal.name()).isEqualTo("Goal #1");
    }

    @Test
    void getWidgetById_shouldReturnWidgetWithGoal_whenWidgetIsGoalWidget() {
        // Set up the workspace and everything that is included in it (widget, goal, goalWidget)
        Workspace workspace = new Workspace();
        workspace.setId(1L);

        Widget widget = new Widget(workspace, 1, WidgetType.GOAL);
        widget.setId(1L);

        workspace.addWidget(widget);

        Goal goal = new Goal();
        goal.setId(1L);
        goal.setName("Goal #1");

        GoalWidget goalWidget = new GoalWidget(widget, goal);
        goalWidget.setId(1L);

        when(widgetRepository.findById(1L)).thenReturn(Optional.of(widget));
        when(goalWidgetRepository.findByWidget(widget)).thenReturn(Optional.of(goalWidget));

        WidgetResponse<?> actual = widgetService.getWidgetById(1L);

        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.content()).isInstanceOf(GoalResponse.class);
        GoalResponse actualGoal = (GoalResponse) actual.content();
        assertThat(actualGoal.id()).isEqualTo(1L);
        assertThat(actualGoal.name()).isEqualTo("Goal #1");
    }

    @Test
    void getWidgetById_shouldThrowWidgetGoalNotFoundException_whenWidgetHasNoGoal() {
        // Set up the workspace and everything that is included in it (widget, goal, goalWidget)
        Workspace workspace = new Workspace();
        workspace.setId(1L);

        Widget widget = new Widget(workspace, 1, WidgetType.GOAL);
        widget.setId(1L);

        workspace.addWidget(widget);

        when(widgetRepository.findById(1L)).thenReturn(Optional.of(widget));
        when(goalWidgetRepository.findByWidget(widget)).thenReturn(Optional.empty());

        GoalWidgetNotFoundException actual = assertThrows(
            GoalWidgetNotFoundException.class,
            () -> widgetService.getWidgetById(1L)
        );

        assertThat(actual.getMessage()).isEqualTo("Widget with ID 1 is not a Goal Widget");
    }

}
