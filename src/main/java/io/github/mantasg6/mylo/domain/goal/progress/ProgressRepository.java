package io.github.mantasg6.mylo.domain.goal.progress;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import io.github.mantasg6.mylo.domain.goal.Goal;



/**
 * Repository for storing Progress in the database.
 *
 */
public interface ProgressRepository extends JpaRepository<Progress, ProgressId> {

    List<Progress> findByGoal(Goal goal);

    @Modifying
    @Query("delete from Progress p where p.id.goalId = :goalId")
    void deleteAllByGoalId(Long goalId);
}
