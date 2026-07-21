package io.github.mantasg6.mylo.domain.workspace;

import io.github.mantasg6.mylo.core.exception.EntityNotFoundException;

/**
 * Exception thrown when a workspace with provided ID is not found.
 *
 */
public class WorkspaceNotFoundException extends EntityNotFoundException {

    public WorkspaceNotFoundException(Long id) {
        super(WorkspaceErrorMessage.notFound(id));
    }

}
