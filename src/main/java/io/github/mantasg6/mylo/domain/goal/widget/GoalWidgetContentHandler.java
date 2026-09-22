package io.github.mantasg6.mylo.domain.goal.widget;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import io.github.mantasg6.mylo.domain.goal.GoalMapper;
import io.github.mantasg6.mylo.domain.goal.GoalResponse;
import io.github.mantasg6.mylo.domain.widget.WidgetContentHandler;
import io.github.mantasg6.mylo.domain.widget.WidgetMapper;
import io.github.mantasg6.mylo.domain.widget.WidgetRequest;
import io.github.mantasg6.mylo.domain.widget.WidgetResponse;
import io.github.mantasg6.mylo.domain.widget.WidgetType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoalWidgetContentHandler implements WidgetContentHandler<GoalResponse> {

    private final GoalWidgetRepository goalWidgetRepository;
    private final WidgetMapper<GoalResponse> widgetMapper;
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
            WidgetResponse<GoalResponse> widgetWithContent = widgetMapper.toDto(
                goalWidget.getWidget(),
                goalMapper.toDto(goalWidget.getGoal())
            );
            result.add(widgetWithContent);
        }
		return result;
	}

	@Override
	public WidgetResponse<GoalResponse> createContent(WidgetRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
