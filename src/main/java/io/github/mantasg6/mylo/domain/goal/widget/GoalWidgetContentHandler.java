package io.github.mantasg6.mylo.domain.goal.widget;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.github.mantasg6.mylo.domain.goal.Goal;
import io.github.mantasg6.mylo.domain.goal.GoalMapper;
import io.github.mantasg6.mylo.domain.goal.GoalNotFoundException;
import io.github.mantasg6.mylo.domain.goal.GoalRepository;
import io.github.mantasg6.mylo.domain.goal.GoalResponse;
import io.github.mantasg6.mylo.domain.widget.Widget;
import io.github.mantasg6.mylo.domain.widget.WidgetContentHandler;
import io.github.mantasg6.mylo.domain.widget.WidgetContentMapper;
import io.github.mantasg6.mylo.domain.widget.WidgetResponse;
import io.github.mantasg6.mylo.domain.widget.WidgetType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoalWidgetContentHandler implements WidgetContentHandler<GoalResponse> {

    private final GoalWidgetRepository goalWidgetRepository;
    private final WidgetContentMapper<GoalResponse> widgetContentMapper;
    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;

	@Override
	public WidgetType getType() {
        return WidgetType.GOAL;
	}

	@Override
	public WidgetResponse<GoalResponse> loadContent(Widget widget) {
        return goalWidgetRepository.findByWidget(widget)
                .map(GoalWidget::getGoal)
                .map(goalMapper::toDto)
                .map(goalResponse -> widgetContentMapper.toDto(widget, goalResponse))
                .orElseGet(() -> widgetContentMapper.toDto(widget, null));
	}

	@Override
	public WidgetResponse<GoalResponse> createContent(Widget widget, Long goalId) {
        Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new GoalNotFoundException(goalId));
        GoalWidget created = goalWidgetRepository.save(new GoalWidget(widget, goal));
		return widgetContentMapper.toDto(created.getWidget(), goalMapper.toDto(created.getGoal()));
	}

	@Override
	public WidgetResponse<GoalResponse> updateContent(Widget widget, Long goalId) {
        Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new GoalNotFoundException(goalId));

        GoalWidget goalWidget = goalWidgetRepository.findByWidget(widget)
                .map(existing -> {
                    if (existing.getGoal().getId().equals(goalId)) {
                        return existing; // it's the same goal, nothing to change
                    }
                    existing.setGoal(goal);
                    return goalWidgetRepository.save(existing);
                })
                .orElseGet(() -> goalWidgetRepository.save(new GoalWidget(widget, goal)));

        return widgetContentMapper.toDto(widget, goalMapper.toDto(goalWidget.getGoal()));
	}

	@Override
	public List<WidgetResponse<GoalResponse>> mapContentList(List<Widget> widgets) {
        List<GoalWidget> goalWidgets = goalWidgetRepository.findByWidgetIn(widgets);
        Map<Long, GoalWidget> goalWidgetsMap = goalWidgets.stream()
                .collect(Collectors.toMap(GoalWidget::getId, Function.identity()));

        return widgets.stream()
                .map(w -> {
                    GoalWidget goalWidget = goalWidgetsMap.get(w.getId());
                    GoalResponse goalResponse = goalWidget == null ? null : goalMapper.toDto(goalWidget.getGoal());
                    return widgetContentMapper.toDto(w, goalResponse);
                })
                .toList();
	}
}
