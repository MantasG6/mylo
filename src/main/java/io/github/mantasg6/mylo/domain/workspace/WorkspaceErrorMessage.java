package io.github.mantasg6.mylo.domain.workspace;

/**
 * All workspace related errors.
 *
 */
public class WorkspaceErrorMessage {

    /**
     * Workspace not found error message builder.
     *
     * @param id ID of the workspace that was not found.
     * @return A message to inform customer that the requested workspace was not found.
     */
    public static final String notFound(Long id) {
        return String.format("Workspace with id %d not found!", id);
    }

}
