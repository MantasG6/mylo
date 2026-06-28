package io.github.mantasg6.mylo.domain.workspace;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(WorkspaceController.WORKSPACES_API)
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public static final String WORKSPACES_API = "/api/v1/workspaces";

    @GetMapping
    public ResponseEntity<List<WorkspaceResponse>> getAll() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }
        
}
