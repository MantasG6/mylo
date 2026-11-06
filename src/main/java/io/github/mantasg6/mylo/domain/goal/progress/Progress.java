package io.github.mantasg6.mylo.domain.goal.progress;

import java.math.BigDecimal;

import io.github.mantasg6.mylo.core.database.BaseEntityNoId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
public class Progress extends BaseEntityNoId {

    @EmbeddedId
    private ProgressId id;

    // TODO: Add goal FK

    @Column(precision = 9, scale = 3, nullable = false)
    private BigDecimal amount;

    public Progress() {}

    public Progress(ProgressId id) {
        this.id = id;
    }
}
