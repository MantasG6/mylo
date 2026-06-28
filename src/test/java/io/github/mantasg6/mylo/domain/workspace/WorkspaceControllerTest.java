package io.github.mantasg6.mylo.domain.workspace;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(WorkspaceController.class)
class WorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkspaceService workspaceService;

    private static final String BASE_ENDPOINT = WorkspaceController.WORKSPACES_API;

    @Test
    void givenNoId_whenGET_returnsOk() throws Exception {
        when(workspaceService.getAllWorkspaces()).thenReturn(new ArrayList<WorkspaceResponse>());

        mockMvc.perform(get(BASE_ENDPOINT))
            .andExpect(status().isOk());
    }

}
