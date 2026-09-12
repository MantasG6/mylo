package io.github.mantasg6.mylo.domain.widget;

import java.time.Instant;

import lombok.Builder;

/**
 * Widget Response DTO.
 *
 */
@Builder
public record WidgetResponse(
    Long id,
    Long workspaceId,
    Integer position,
    Instant createdAt,
    Instant updatedAt
) {}
