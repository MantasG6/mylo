package io.github.mantasg6.mylo.domain.workspace;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

/**
 * Workspace request DTO.
 *
 */
@ValidWorkspacePeriod
@Builder
public record WorkspaceRequest(
    @NotBlank(message = "Workspace name cannot be blank")
    String name,

    @NotNull(message = "The start of the period must be provided")
    @PastOrPresent(message = "Period start cannot be in the future")
    LocalDate periodStart,

    @NotNull(message = "The end of the period must be provided")
    LocalDate periodEnd
) { }
