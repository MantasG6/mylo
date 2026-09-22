package io.github.mantasg6.mylo.domain.goal.widget;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.mantasg6.mylo.domain.widget.Widget;


/**
 * Repository to store Goal references to Widgets in the database.
 *
 */
public interface GoalWidgetRepository extends JpaRepository<GoalWidget, Long> {
    Optional<GoalWidget> findByWidget(Widget widget);
}
