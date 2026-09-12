package io.github.mantasg6.mylo.domain.workspace;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import io.github.mantasg6.mylo.domain.widget.WidgetResponse;
import lombok.Builder;

/**
 * Workspace response DTO.
 *
 */
@Builder
public record WorkspaceResponse(
    Long id,
    String name,
    List<WidgetResponse> widgets,
    LocalDate periodStart,
    LocalDate periodEnd,
    Instant createdAt,
    Instant updatedAt
) { }
