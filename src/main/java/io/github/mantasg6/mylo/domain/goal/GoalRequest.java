package io.github.mantasg6.mylo.domain.goal;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
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
        message = "Goal name must be 1-25 characters",
        groups = { OnCreate.class, OnUpdate.class }
    )
    String name,

    @NotNull(
        message = "Goal must have a numerical annual target",
        groups = OnCreate.class
    )
    @Digits(
        integer = 9,
        fraction = 3,
        message = "Choose a target up to 1 billion with 3 decimal places at most",
        groups = { OnCreate.class, OnUpdate.class }
    )
    BigDecimal annualTarget
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
