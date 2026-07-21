package io.github.mantasg6.mylo.domain.workspace;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import io.github.mantasg6.mylo.domain.workspace.util.WorkspaceTestFactory;

/**
 * Workspace API HTTP contract tests
 *
 */
@WebMvcTest(WorkspaceController.class)
public class WorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkspaceService workspaceService;

    private static final String BASE_ENDPOINT = WorkspaceController.WORKSPACES_API;
    private static final String ENDPOINT_WITH_ID = BASE_ENDPOINT + "/" +
            WorkspaceTestFactory.VALID_ID;
    public static final String ENDPOINT_WITH_INVALID_ID = BASE_ENDPOINT + "/" +
            WorkspaceTestFactory.INVALID_ID;

    @Test
    void shouldReturnOk_whenGetWithNoId() throws Exception {
        List<WorkspaceResponse> workspaces = List.of(
                WorkspaceTestFactory.defaultWorkspaceResponse(),
                WorkspaceTestFactory.updateWorkspaceResponse());
        when(workspaceService.getAllWorkspaces())
                .thenReturn(workspaces);

        mockMvc.perform(get(BASE_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnOk_whenGetWithId() throws Exception {
        when(workspaceService.getWorkspaceById(WorkspaceTestFactory.VALID_ID))
                .thenReturn(WorkspaceTestFactory.defaultWorkspaceResponse());

        ResultActions result = mockMvc.perform(get(ENDPOINT_WITH_ID));

        WorkspaceTestFactory.assertDefault(result);
    }

    @Test
    void shouldReturnNotFound_whenGetWithInvalidId() throws Exception {
        Long id = WorkspaceTestFactory.INVALID_ID;
        when(workspaceService.getWorkspaceById(id))
                .thenThrow(new WorkspaceNotFoundException(id));

        ResultActions result = mockMvc.perform(get(ENDPOINT_WITH_INVALID_ID));

        WorkspaceTestFactory.assertNotFound(result);
    }

}
