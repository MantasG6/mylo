package io.github.mantasg6.mylo.domain.goal.widget;

import io.github.mantasg6.mylo.core.database.BaseEntity;
import io.github.mantasg6.mylo.domain.goal.Goal;
import io.github.mantasg6.mylo.domain.widget.Widget;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Goal Widget intermediary table that helps to map Goal Widgets to user workspaces.
 *
 */
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "goal_widgets")
public class GoalWidget extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "widget_id", nullable = false, unique = true)
    private Widget widget;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Goal goal;
}
