package io.github.mantasg6.mylo.domain.workspace;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import io.github.mantasg6.mylo.domain.widget.Widget;
import lombok.Builder;

/**
 * Workspace response DTO.
 *
 */
@Builder
public record WorkspaceResponse(
    String name,
    List<Widget> widgets,
    LocalDate periodFrom,
    LocalDate periodTo,
    Instant createdAt,
    Instant updatedAt
) { }
