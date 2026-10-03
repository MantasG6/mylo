package io.github.mantasg6.mylo.domain.widget;

import java.time.Instant;

import lombok.Builder;

/**
 * Widget Response DTO.
 *
 */
@Builder
public record WidgetResponse<T>(
    Long id,
    Long workspaceId,
    Integer position,
    WidgetType type,
    Instant createdAt,
    Instant updatedAt,
    T content
) {}
