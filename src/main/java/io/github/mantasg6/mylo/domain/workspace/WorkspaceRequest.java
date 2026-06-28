package io.github.mantasg6.mylo.domain.workspace;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record WorkspaceRequest(
    String name,
    LocalDateTime from,
    LocalDateTime to
) { }
