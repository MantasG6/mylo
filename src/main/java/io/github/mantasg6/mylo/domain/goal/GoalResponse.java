package io.github.mantasg6.mylo.domain.goal;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Builder;

/**
 * Goal Response DTO.
 *
 */
@Builder
public record GoalResponse(
    Long id,
    String name,
    BigDecimal target,
    Instant createdAt,
    Instant updatedAt
) {}
