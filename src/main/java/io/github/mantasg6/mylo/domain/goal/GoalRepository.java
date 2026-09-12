package io.github.mantasg6.mylo.domain.goal;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository that represents data layer for Goal database table.
 *
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {}
