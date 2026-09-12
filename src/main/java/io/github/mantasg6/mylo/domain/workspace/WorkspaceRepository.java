package io.github.mantasg6.mylo.domain.workspace;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for storing user Workspaces in the database.
 *
 */
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {}
