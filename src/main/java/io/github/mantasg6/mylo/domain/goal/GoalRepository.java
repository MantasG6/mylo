package io.github.mantasg6.mylo.domain.goal;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for storing Goals in the database.
 *
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {}
