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
    @NotNull(
        message = "Widget position is required",
        groups = {OnCreate.class}
    )
    Integer position,

    @NotNull(
        message = "Widget must have a reference to content",
        groups = {OnCreate.class, OnUpdate.class}
    )
    Long contentId
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
