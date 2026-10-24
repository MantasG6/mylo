package io.github.mantasg6.mylo.domain.goal.progress;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * Progress Request DTO.
 *
 */
@Builder
public record ProgressRequest(
    @NotNull(
        message = "Amount is required"
    )
    @Digits(
        integer = 9,
        fraction = 3,
        message = "Amount must be up to 1 billion with 3 decimal places at most"
    )
    BigDecimal amount
) {}
