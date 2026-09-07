package io.github.mantasg6.mylo.domain.workspace;

import java.time.LocalDate;

import io.github.mantasg6.mylo.domain.workspace.WorkspaceRequest.OnCreate;
import io.github.mantasg6.mylo.domain.workspace.WorkspaceRequest.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Workspace request DTO.
 *
 */
@ValidWorkspacePeriod(groups = { OnCreate.class, OnUpdate.class })
@Builder
public record WorkspaceRequest(
    @Size(
        min = 1, max = 20,
        message = "Workspace name length must be 1-20 characters",
        groups = { OnCreate.class, OnUpdate.class }
    )
    String name,

    @NotNull(message = "The start of the period must be provided", groups = OnCreate.class)
    @PastOrPresent(message = "Period start cannot be in the future", groups = { OnCreate.class, OnUpdate.class })
    LocalDate periodStart,

    @NotNull(message = "The end of the period must be provided", groups = OnCreate.class)
    LocalDate periodEnd
) {
    public interface OnCreate {}
    public interface OnUpdate {}
}
