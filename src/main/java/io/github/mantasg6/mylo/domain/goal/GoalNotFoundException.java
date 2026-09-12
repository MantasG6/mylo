package io.github.mantasg6.mylo.domain.goal;

import io.github.mantasg6.mylo.core.exception.EntityNotFoundException;

/**
 * Exception thrown when a Goal with provided ID is not found.
 *
 */
public class GoalNotFoundException extends EntityNotFoundException {
    public GoalNotFoundException(Long id) {
        super(GoalErrorMessage.GOAL_NOT_FOUND(id));
    }
}
