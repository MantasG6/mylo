package io.github.mantasg6.mylo.domain.widget;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

/**
 * Widget Create Request DTO.
 *
 */
@Builder
public record WidgetCreateRequest(
    @NotNull(message = "Widget must belong to a workspace")
    Long workspaceId,

    @NotNull(message = "Widget must have a type")
    @Enumerated(EnumType.STRING)
    WidgetType type,

    @Positive(message = "Widget position must be greater than 0")
    @NotNull(message = "Widget position is required")
    Integer position,

    @NotNull(message = "Widget must have a reference to content")
    Long contentId
) {}
