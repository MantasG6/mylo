package io.github.mantasg6.mylo.domain.workspace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;


/**
 * Workspace API HTTP contract tests
 *
 */
@WebMvcTest(WorkspaceController.class)
@AutoConfigureRestTestClient
public class WorkspaceControllerTest {

    @Autowired
    RestTestClient restTestClient;

    @MockitoBean
    private WorkspaceService workspaceService;

    @Test
    void shouldReturnAll_whenGetWithNoId() throws Exception {
        List<WorkspaceResponse> workspaces = List.of(
                WorkspaceResponse.builder().name("workspace1").build(),
                WorkspaceResponse.builder().name("workspace2").build(),
                WorkspaceResponse.builder().name("workspace3").build()
        );
        when(workspaceService.getAllWorkspaces())
                .thenReturn(workspaces);

        List<WorkspaceResponse> response = restTestClient.get().uri("/api/workspaces")
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<WorkspaceResponse>>() {})
            .returnResult()
            .getResponseBody();

        assertThat(response).hasSize(3);
    }

    @Test
    void shouldReturnSingleWorkspace_whenGetWithId() throws Exception {
        WorkspaceResponse expected = WorkspaceResponse.builder()
                .name("expectedWorkspace")
                .periodFrom(LocalDate.of(2020, 06, 20))
                .periodTo(LocalDate.of(2020, 06, 21))
                .build();
        when(workspaceService.getWorkspaceById(1L))
                .thenReturn(expected);

        WorkspaceResponse response = restTestClient.get().uri("/api/workspaces/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(WorkspaceResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response.name()).isEqualTo("expectedWorkspace");
        assertThat(response.periodFrom()).isEqualTo(LocalDate.of(2020, 06, 20));
        assertThat(response.periodTo()).isEqualTo(LocalDate.of(2020, 06, 21));
    }

    @Test
    void shouldReturnNotFound_whenGetWithInvalidId() throws Exception {
        Long invalid_id = -1L;
        when(workspaceService.getWorkspaceById(invalid_id))
                .thenThrow(new WorkspaceNotFoundException(invalid_id));

        ProblemDetail response = restTestClient.get().uri("/api/workspaces/-1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(response.getDetail()).isEqualTo("Workspace with id -1 not found!");
        assertThat(response.getInstance()).isEqualTo(URI.create("/api/workspaces/-1"));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

}
