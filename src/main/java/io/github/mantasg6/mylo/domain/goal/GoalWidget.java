package io.github.mantasg6.mylo.domain.goal;

import io.github.mantasg6.mylo.domain.widget.Widget;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Goal Widget intermediary table that helps to map Goal Widgets to user workspaces.
 *
 */
@Setter
@Getter
@Entity
@Table(name = "goal_widgets")
public class GoalWidget extends Widget {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Goal goal;
}
