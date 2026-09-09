package io.github.mantasg6.mylo.domain.workspace;

import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WorkspaceServiceTest {

    @Mock private WorkspaceRepository repository;
    @Mock private WorkspaceMapper mapper;

    @InjectMocks private WorkspaceService service;

    @Test
    void updateWorkspace_shouldThrowWorkspaceUpdateException_whenPeriodStartAfterPeriodEnd() {
        WorkspaceRequest updateRequest = WorkspaceRequest.builder()
                .periodStart(LocalDate.of(2000, 1, 12))
                .build();
        Workspace existingWorkspace = new Workspace();
        existingWorkspace.setName("Workspace");
        existingWorkspace.setPeriodStart(LocalDate.of(2000, 1, 1));
        existingWorkspace.setPeriodStart(LocalDate.of(2000, 1, 1));
        when(repository.findById(1L)).thenReturn()
    }

}
