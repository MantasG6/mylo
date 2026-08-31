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

    public List<WorkspaceResponse> getAllWorkspaces() {
        return workspaceRepository.findAll().stream()
                .map(workspaceMapper::toDto)
                .toList();
    }

    public WorkspaceResponse getWorkspaceById(Long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException(id));
        return workspaceMapper.toDto(workspace);
    }

    public WorkspaceResponse createWorkspace(WorkspaceRequest request) {
        Workspace newWorkspace = workspaceMapper.toEntity(request);
        return workspaceMapper.toDto(workspaceRepository.save(newWorkspace));
    }

    public WorkspaceResponse updateWorkspace(Long id, WorkspaceRequest request) {
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new WorkspaceNotFoundException(id));

        Optional.ofNullable(request.name()).ifPresent(workspace::setName);
        Optional.ofNullable(request.periodStart()).ifPresent(workspace::setPeriodStart);
        Optional.ofNullable(request.periodEnd()).ifPresent(workspace::setPeriodEnd);

        return workspaceMapper.toDto(workspaceRepository.save(workspace));
    }

    public void deleteWorkspace(Long id) {
        workspaceRepository.deleteById(id);
    }
}
