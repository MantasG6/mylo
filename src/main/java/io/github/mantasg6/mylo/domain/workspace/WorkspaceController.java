package io.github.mantasg6.mylo.domain.workspace;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;



/**
 * Workspace controller describes API HTTP contract for workspaces.
 *
 */
@RestController
@RequestMapping(WorkspaceController.WORKSPACES_API)
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public static final String WORKSPACES_API = "/api/workspaces";

    /**
     * Retrieve all existing workspaces.
     *
     * @return HTTP 200 and the list of existing workspaces.
     */
    @GetMapping
    public ResponseEntity<List<WorkspaceResponse>> getAll() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }

    /**
     * Retrieve a single workspace by id.
     *
     * @param id ID of the workspace to retrieve.
     * @return HTTP 200 and the details of the retrieved workspace.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(workspaceService.getWorkspaceById(id));
    }

    /**
     * Create a user workspace.
     *
     * @param request Workspace request containing required details about the workspace.
     * @return HTTP response 201 with the created workspace in the body.
     */
    @PostMapping()
    public ResponseEntity<WorkspaceResponse> createWorkspace(@RequestBody WorkspaceRequest request) {
        WorkspaceResponse created = workspaceService.createWorkspace(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    /**
     * Update a user workspace.
     *
     * @param id ID of the user workspace to be updated.
     * @param request Details of the new workspace.
     * @return HTTP 200 with Updated user workspace.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkspaceResponse> updateWorkspace(
            @PathVariable Long id,
            @RequestBody WorkspaceRequest request) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(id, request));
    }

    /**
     * Delete a user workspace.
     *
     * @param id ID of the user workspace to delete.
     * @return 204 repsonce entity.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();
    }
        
}
