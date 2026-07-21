package io.github.mantasg6.mylo.domain.workspace;

import java.time.LocalDate;

import lombok.Builder;

/**
 * Workspace request DTO.
 *
 */
@Builder
public record WorkspaceRequest(
    String name,
    LocalDate periodFrom,
    LocalDate periodTo
) { }
