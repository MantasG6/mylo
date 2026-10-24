package io.github.mantasg6.mylo.domain.goal.progress;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import io.github.mantasg6.mylo.domain.goal.Goal;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
public class Progress {

    @EmbeddedId
    private ProgressId id;

    @MapsId("goalId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Goal goal;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "user_id")
    private UUID userId;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
