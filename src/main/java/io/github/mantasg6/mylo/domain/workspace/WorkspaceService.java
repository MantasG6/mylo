package io.github.mantasg6.mylo.domain.workspace;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Workspace management service.
 *
 */
@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;

    /**
     * Get all workspaces from the database.
     *
     * @return List of all workspaces.
     */
    public List<WorkspaceResponse> getAllWorkspaces() {
        return workspaceRepository.findAll().stream()
                .map(workspaceMapper::toDto)
                .toList();
    }

    /**
     * Get a single workspace by id.
     *
     * @param id ID of the workspace.
     * @return Workspace details of the workspace with the provided id.
     */
    public WorkspaceResponse getWorkspaceById(Long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException(id));
        return workspaceMapper.toDto(workspace);
    }

    /**
     * Creates a new workspace with the details provided in the request.
     *
     * @param request Workspace request containing all the details for the new workspace.
     * @return Workspace response containing all the details of the new workspace.
     */
    public WorkspaceResponse createWorkspace(WorkspaceRequest request) {
        Workspace newWorkspace = workspaceMapper.toEntity(request);
        return workspaceMapper.toDto(workspaceRepository.save(newWorkspace));
    }

    /**
     * Updates workspace with the provided id.
     *
     * @param id ID of the workspace to update.
     * @param request Workspace details to be updated.
     * @return New workspace details.
     */
    public WorkspaceResponse updateWorkspace(Long id, WorkspaceRequest request) {
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new WorkspaceNotFoundException(id));

        Optional.ofNullable(request.name()).ifPresent(workspace::setName);
        Optional.ofNullable(request.periodStart()).ifPresent(workspace::setPeriodStart);
        Optional.ofNullable(request.periodEnd()).ifPresent(workspace::setPeriodEnd);

        return workspaceMapper.toDto(workspaceRepository.save(workspace));
    }

    /**
     * Deletes the workspace with provided id.
     *
     * @param id ID of the workspace to delete.
     */
    public void deleteWorkspace(Long id) {
        workspaceRepository.deleteById(id);
    }
}
