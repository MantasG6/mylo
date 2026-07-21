package io.github.mantasg6.mylo.domain.workspace;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Workspace management service.
 *
 */
@Service
@RequiredArgsConstructor
public class WorkspaceService {

    public List<WorkspaceResponse> getAllWorkspaces() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public WorkspaceResponse getWorkspaceById(Long id) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public WorkspaceResponse createWorkspace(WorkspaceRequest request) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public WorkspaceResponse updateWorkspace(Long id, WorkspaceRequest request) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteWorkspace(Long id) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
