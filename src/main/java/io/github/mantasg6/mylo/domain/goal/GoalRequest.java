package io.github.mantasg6.mylo.domain.goal;

import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Goal Request DTO.
 *
 */
@Builder
public record GoalRequest(
    @Size(
        min = 1, max = 25,
        message = "Goal name must be 1-25 characters"
    )
    String name
) {}
