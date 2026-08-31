package io.github.mantasg6.mylo.domain.workspace;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository that represents data layer for a user workspace.
 *
 */
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {}
