package io.github.mantasg6.mylo.domain.workspace;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Workspace request DTO.
 *
 */
@ValidWorkspacePeriod
@Builder
public record WorkspaceRequest(
    @Size(min = 1, max = 20, message = "Workspace name length must be 1-20 characters")
    String name,

    @NotNull(message = "The start of the period must be provided")
    @PastOrPresent(message = "Period start cannot be in the future")
    LocalDate periodStart,

    @NotNull(message = "The end of the period must be provided")
    LocalDate periodEnd
) { }
