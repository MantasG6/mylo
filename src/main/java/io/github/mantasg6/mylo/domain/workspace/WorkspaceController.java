package io.github.mantasg6.mylo.domain.workspace;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


/**
 * Workspace controller describes API HTTP contract for workspaces
 *
 */
@RestController
@RequestMapping(WorkspaceController.WORKSPACES_API)
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public static final String WORKSPACES_API = "/api/v1/workspaces";

    /**
     * Retrieve all existing workspaces
     *
     * @return List of existing workspaces
     */
    @GetMapping
    public ResponseEntity<List<WorkspaceResponse>> getAll() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }
        
}
