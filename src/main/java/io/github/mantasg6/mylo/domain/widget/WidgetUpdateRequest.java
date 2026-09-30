package io.github.mantasg6.mylo.domain.widget;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

/**
 * Widget Update Request DTO.
 *
 */
@Builder
public record WidgetUpdateRequest(
    @Positive(message = "Widget position must be greater than 0")
    @NotNull(message = "Widget position is required")
    Integer position
) {}
