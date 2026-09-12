package io.github.mantasg6.mylo.domain.workspace;

import java.time.LocalDate;

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
    public static final String NOT_FOUND(Long id) {
        return String.format("Workspace with id %d not found!", id);
    }

    public static final String PERIOD_START_AFTER_END(LocalDate start, LocalDate end) {
        return String.format(
            "Workspace period start (%tY-%<tm-%<td) cannot be after the period end(%tY-%<tm-%<td)!",
            start, end
        );
    } ;

}
