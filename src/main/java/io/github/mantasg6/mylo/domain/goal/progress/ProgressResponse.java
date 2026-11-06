package io.github.mantasg6.mylo.domain.goal.progress;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Builder;

/**
 * Progress Response DTO.
 *
 */
@Builder
public record ProgressResponse(
    ProgressId id,
    BigDecimal amount,
    Instant createdAt,
    Instant updatedAt
) {}
