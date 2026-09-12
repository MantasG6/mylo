package io.github.mantasg6.mylo.domain.goal;

import java.time.Instant;
import java.util.List;

import io.github.mantasg6.mylo.domain.progress.Progress;
import lombok.Builder;

/**
 * Goal Response DTO.
 *
 */
@Builder
public record GoalResponse(
    Long id,
    String name,
    List<Progress> progress,
    Instant createdAt,
    Instant updatedAt
) {}
