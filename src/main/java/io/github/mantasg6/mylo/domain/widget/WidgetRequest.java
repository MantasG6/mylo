package io.github.mantasg6.mylo.domain.widget;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

/**
 * Widget Request DTO.
 *
 */
@Builder
public record WidgetRequest(
    @NotNull(
        message = "Widget must belong to a workspace",
        groups = OnCreate.class
    )
    Long workspaceId,

    @Positive(
        message = "Widget position must be greater than 0",
        groups = {OnCreate.class, OnUpdate.class}
    )
    Integer position
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
