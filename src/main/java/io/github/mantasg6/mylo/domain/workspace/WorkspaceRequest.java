package io.github.mantasg6.mylo.domain.workspace;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Workspace request DTO.
 *
 */
@Builder
public record WorkspaceRequest(
    @Size(
        min = 1, max = 20,
        message = "Workspace name length must be 1-20 characters"
    )
    @NotNull(message = "Workspace must have a name")
    String name
) {}
