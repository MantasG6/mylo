package io.github.mantasg6.mylo.domain.workspace;

import java.time.LocalDateTime;
import java.util.List;

import io.github.mantasg6.mylo.domain.widget.Widget;
import lombok.Builder;

@Builder
public record WorkspaceResponse(
    String name,
    List<Widget> widgets,
    LocalDateTime from,
    LocalDateTime to,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) { }
