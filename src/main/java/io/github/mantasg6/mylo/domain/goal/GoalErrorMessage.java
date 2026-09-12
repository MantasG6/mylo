package io.github.mantasg6.mylo.domain.goal;

/**
 * All goal related errors.
 *
 */
public class GoalErrorMessage {

    /**
     * Goal not found error.
     *
     * @param id ID of the goal that was not found.
     * @return Error message to the user.
     */
    public static final String GOAL_NOT_FOUND(Long id) {
        return String.format("Goal with id %d not found!", id);
    }
}
