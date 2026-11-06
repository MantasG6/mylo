package io.github.mantasg6.mylo.domain.goal.progress;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * A composite key for Progress ID.
 *
 */
@Embeddable
public record ProgressId(

    @Column(name = "goal_id", nullable = false)
    Long goalId,

    @Column(name = "effective_date", nullable = false)
    LocalDate effectiveDate

) implements Serializable {}
