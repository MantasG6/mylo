package io.github.mantasg6.mylo.domain.goal.widget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import io.github.mantasg6.mylo.domain.goal.GoalResponse;
import io.github.mantasg6.mylo.domain.widget.Widget;
import io.github.mantasg6.mylo.domain.widget.WidgetMapper;
import io.github.mantasg6.mylo.domain.widget.WidgetRepository;
import io.github.mantasg6.mylo.domain.widget.WidgetResponse;
import io.github.mantasg6.mylo.domain.widget.WidgetService;
import io.github.mantasg6.mylo.domain.widget.WidgetType;
import io.github.mantasg6.mylo.domain.workspace.Workspace;

@ExtendWith(MockitoExtension.class)
public class GoalWidgetHandlerTest {

    @Mock
    private WidgetRepository widgetRepository;

    @Mock
    private GoalWidgetRepository goalWidgetRepository;

    private WidgetMapper<GoalResponse> widgetMapper;

    private GoalMapper goalMapper;

    private GoalWidgetContentHandler goalHandler;

    private WidgetService widgetService;

    @BeforeEach
    void setUp() {
        widgetMapper = Mappers.getMapper(GoalWidgetMapper.class);
        goalMapper = Mappers.getMapper(GoalMapper.class);
        goalHandler = new GoalWidgetContentHandler(goalWidgetRepository, widgetMapper, goalMapper);
        widgetService = new WidgetService(widgetRepository, List.of(goalHandler));
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

        GoalWidget goalWidget = new GoalWidget();
        goalWidget.setId(1L);
        goalWidget.setWidget(widget);
        goalWidget.setGoal(goal);

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

        GoalWidget goalWidget = new GoalWidget();
        goalWidget.setId(1L);
        goalWidget.setWidget(widget);
        goalWidget.setGoal(goal);

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
