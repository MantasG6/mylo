package io.github.mantasg6.mylo.domain.goal.widget;

import java.util.ArrayList;
import java.util.List;

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
	public List<WidgetResponse<GoalResponse>> loadAllContent() {
        List<WidgetResponse<GoalResponse>> result = new ArrayList<>();

        List<GoalWidget> allGoalWidgets = goalWidgetRepository.findAll();

        for (GoalWidget goalWidget : allGoalWidgets) {
            WidgetResponse<GoalResponse> widgetWithContent = widgetContentMapper.toDto(
                goalWidget.getWidget(),
                goalMapper.toDto(goalWidget.getGoal())
            );
            result.add(widgetWithContent);
        }
		return result;
	}

	@Override
	public WidgetResponse<GoalResponse> loadContent(Widget widget) {
        GoalWidget goalWidget = goalWidgetRepository.findByWidget(widget)
                .orElseThrow(() -> new GoalWidgetNotFoundException(widget.getId()));
        return widgetContentMapper.toDto(widget, goalMapper.toDto(goalWidget.getGoal()));
	}

	@Override
	public WidgetResponse<GoalResponse> createContent(Widget widget, Long goalId) {
        Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new GoalNotFoundException(goalId));
        GoalWidget created = goalWidgetRepository.save(new GoalWidget(widget, goal));
		return widgetContentMapper.toDto(created.getWidget(), goalMapper.toDto(created.getGoal()));
	}
}
