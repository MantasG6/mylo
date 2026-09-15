package io.github.mantasg6.mylo.domain.progress;

import io.github.mantasg6.mylo.core.database.BaseEntity;
import io.github.mantasg6.mylo.domain.goal.Goal;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Progress entity representing progress database table.
 *
 */
@Setter
@Getter
@Entity
@Table(name = "progress")
public class Progress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Goal goal;

    private Long amount;
}
