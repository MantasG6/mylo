package io.github.mantasg6.mylo.domain.goal.widget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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
import io.github.mantasg6.mylo.domain.goal.GoalNotFoundException;
import io.github.mantasg6.mylo.domain.goal.GoalRepository;
import io.github.mantasg6.mylo.domain.goal.GoalResponse;
import io.github.mantasg6.mylo.domain.widget.Widget;
import io.github.mantasg6.mylo.domain.widget.WidgetContentMapper;
import io.github.mantasg6.mylo.domain.widget.WidgetRepository;
import io.github.mantasg6.mylo.domain.widget.WidgetResponse;
import io.github.mantasg6.mylo.domain.widget.WidgetType;
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

    private GoalMapper goalMapper;

    private GoalWidgetContentHandler goalHandler;

    private Widget widget;

    private Goal goal;

    private GoalWidget goalWidget;

    @BeforeEach
    void setUp() {
        widgetContentMapper = Mappers.getMapper(GoalWidgetMapper.class);
        goalMapper = Mappers.getMapper(GoalMapper.class);
        goalHandler = new GoalWidgetContentHandler(
                goalWidgetRepository, widgetContentMapper,
                goalRepository, goalMapper
        );

        widget = new Widget();
        widget.setId(1L);
        widget.setType(WidgetType.GOAL);
        widget.setPosition(1);

        goal = new Goal();
        goal.setId(11L);
        goal.setName("Goal #1");

        goalWidget = new GoalWidget(widget, goal);
        goalWidget.setId(21L);
    }

    @Test
    void loadContent_shouldReturnWidgetWithContent_whenGoalWidgetExists() {
        when(goalWidgetRepository.findByWidget(widget)).thenReturn(Optional.of(goalWidget));
        
        WidgetResponse<GoalResponse> actual = goalHandler.loadContent(widget);

        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.type()).isEqualTo(WidgetType.GOAL);
        assertThat(actual.position()).isEqualTo(1);
        assertThat(actual.content().id()).isEqualTo(11L);
        assertThat(actual.content().name()).isEqualTo("Goal #1");
    }

    @Test
    void loadContent_shouldReturnPlainWidget_whenGoalWidgetNotFound() {
        when(goalWidgetRepository.findByWidget(widget)).thenReturn(Optional.empty());

        WidgetResponse<GoalResponse> actual = goalHandler.loadContent(widget);

        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.type()).isEqualTo(WidgetType.GOAL);
        assertThat(actual.position()).isEqualTo(1);
        assertThat(actual.content()).isNull();
    }

    @Test
    void createContent_shouldThrowGoalNotFoundException_whenGoalWithProvidedIdNotFound() {
        when(goalRepository.findById(11L)).thenReturn(Optional.empty());

        GoalNotFoundException actual = assertThrows(
                GoalNotFoundException.class,
                () -> goalHandler.createContent(widget, 11L)
        );

        assertThat(actual.getMessage()).isEqualTo("Goal with id 11 not found!");
    }

    @Test
    void createContent_shouldReturnWidgetWithContent_whenSuccess() {
        when(goalRepository.findById(11L)).thenReturn(Optional.of(goal));
        when(goalWidgetRepository.save(any(GoalWidget.class))).thenReturn(goalWidget);

        WidgetResponse<GoalResponse> actual = goalHandler.createContent(widget, 11L);

        verify(goalWidgetRepository).save(any(GoalWidget.class));
        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.type()).isEqualTo(WidgetType.GOAL);
        assertThat(actual.position()).isEqualTo(1);
        assertThat(actual.content().id()).isEqualTo(11L);
        assertThat(actual.content().name()).isEqualTo("Goal #1");
    }

    @Test
    void updateContent_shouldReturnGoalNotFoundException_whenGoalWithProvidedIdNotFound() {
        when(goalRepository.findById(11L)).thenReturn(Optional.empty());

        GoalNotFoundException actual = assertThrows(
                GoalNotFoundException.class,
                () -> goalHandler.createContent(widget, 11L)
        );

        assertThat(actual.getMessage()).isEqualTo("Goal with id 11 not found!");
    }

    @Test
    void updateContent_shouldCreateNewGoalWidgetReference_whenGoalWidgetWithProvidedIdNotFound() {
        when(goalRepository.findById(11L)).thenReturn(Optional.of(goal));
        when(goalWidgetRepository.findByWidget(widget)).thenReturn(Optional.empty());
        when(goalWidgetRepository.save(any(GoalWidget.class))).thenReturn(goalWidget);

        WidgetResponse<GoalResponse> actual = goalHandler.updateContent(widget, 11L);

        verify(goalWidgetRepository).save(any(GoalWidget.class));
        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.type()).isEqualTo(WidgetType.GOAL);
        assertThat(actual.position()).isEqualTo(1);
        assertThat(actual.content().id()).isEqualTo(11L);
        assertThat(actual.content().name()).isEqualTo("Goal #1");
    }

    @Test
    void updateContent_shouldReturnExisting_whenGoalNotChanged() {
        when(goalRepository.findById(11L)).thenReturn(Optional.of(goal));
        when(goalWidgetRepository.findByWidget(widget)).thenReturn(Optional.of(goalWidget));

        WidgetResponse<GoalResponse> actual = goalHandler.updateContent(widget, 11L);

        verify(goalWidgetRepository, never()).save(any());
        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.type()).isEqualTo(WidgetType.GOAL);
        assertThat(actual.position()).isEqualTo(1);
        assertThat(actual.content().id()).isEqualTo(11L);
        assertThat(actual.content().name()).isEqualTo("Goal #1");
    }

    @Test
    void updateContent_shouldCreateNewGoalWidgetReference_whenNewGoalIdIsProvided() {
        Goal newGoal = new Goal();
        newGoal.setId(12L);
        newGoal.setName("Goal #2");
        GoalWidget newGoalWidget = new GoalWidget(widget, newGoal);
        when(goalRepository.findById(12L)).thenReturn(Optional.of(newGoal));
        when(goalWidgetRepository.findByWidget(widget)).thenReturn(Optional.of(goalWidget));
        when(goalWidgetRepository.save(any(GoalWidget.class))).thenReturn(newGoalWidget);

        WidgetResponse<GoalResponse> actual = goalHandler.updateContent(widget, 12L);

        verify(goalWidgetRepository).save(any(GoalWidget.class));
        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.type()).isEqualTo(WidgetType.GOAL);
        assertThat(actual.content().id()).isEqualTo(12L);
        assertThat(actual.content().name()).isEqualTo("Goal #2");
    }

    @Test
    void mapContentList_shouldReturnBothPlainWidgetsAndWithContent_whenNotAllWidgetsHaveContent() {
        Widget plainWidget = new Widget();
        plainWidget.setId(2L);
        plainWidget.setType(WidgetType.GOAL);
        plainWidget.setPosition(2);
        List<Widget> widgets = List.of(widget, plainWidget);
        List<GoalWidget> goalWidgets = List.of(goalWidget);
        when(goalWidgetRepository.findByWidgetIn(widgets)).thenReturn(goalWidgets);

        List<WidgetResponse<GoalResponse>> actual = goalHandler.mapContentList(widgets);

        WidgetResponse<GoalResponse> expectedWithContent = WidgetResponse.<GoalResponse>builder()
                .id(1L).type(WidgetType.GOAL).position(1)
                .content(
                        GoalResponse.builder()
                                .id(11L)
                                .name("Goal #1")
                                .progress(new ArrayList<>())
                                .build()
                ).build();
        WidgetResponse<GoalResponse> expectedPlain = WidgetResponse.<GoalResponse>builder()
                .id(2L).type(WidgetType.GOAL).position(2).content(null).build();
        assertThat(actual).containsExactlyInAnyOrder(expectedWithContent, expectedPlain);
    }
}
