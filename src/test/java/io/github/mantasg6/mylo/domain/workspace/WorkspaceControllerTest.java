package io.github.mantasg6.mylo.domain.workspace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

import io.github.mantasg6.mylo.core.exception.ValidationProblemDetail;


/**
 * Workspace API HTTP contract tests
 *
 */
@WebMvcTest(WorkspaceController.class)
@AutoConfigureRestTestClient
public class WorkspaceControllerTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private WorkspaceService workspaceService;

    @Test
    void GET_shouldReturnAll_whenNoId() {
        List<WorkspaceResponse> workspaces = List.of(
                WorkspaceResponse.builder().name("workspace1").build(),
                WorkspaceResponse.builder().name("workspace2").build(),
                WorkspaceResponse.builder().name("workspace3").build()
        );
        when(workspaceService.getAllWorkspaces())
                .thenReturn(workspaces);

        List<WorkspaceResponse> actual = restTestClient.get().uri("/api/workspaces")
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<WorkspaceResponse>>() {})
            .returnResult()
            .getResponseBody();

        assertThat(actual).hasSize(3);
    }

    @Test
    void GET_shouldReturnSingleWorkspace_whenWithId() {
        WorkspaceResponse expected = WorkspaceResponse.builder()
                .name("expectedWorkspace")
                .build();
        when(workspaceService.getWorkspaceById(1L))
                .thenReturn(expected);

        WorkspaceResponse actual = restTestClient.get().uri("/api/workspaces/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(WorkspaceResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.name()).isEqualTo("expectedWorkspace");
    }

    @Test
    void GET_shouldReturnNotFound_whenInvalidId() {
        Long invalidId = 999L;
        when(workspaceService.getWorkspaceById(invalidId))
                .thenThrow(new WorkspaceNotFoundException(invalidId));

        ProblemDetail actual = restTestClient.get().uri("/api/workspaces/999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Workspace with id 999 not found!");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/workspaces/999"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @Test
    void POST_shouldReturnCreated_whenValidRequest() {
        Long id = 1L;
        String name = "workspace";
        WorkspaceRequest request = WorkspaceRequest.builder()
                .name(name)
                .build();
        WorkspaceResponse expected = WorkspaceResponse.builder()
                .id(id)
                .name(name)
                .build();
        when(workspaceService.createWorkspace(request))
                .thenReturn(expected);

        EntityExchangeResult<WorkspaceResponse> result = restTestClient.post().uri("/api/workspaces")
                .body(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(WorkspaceResponse.class)
                .returnResult();
        URI location = result.getResponseHeaders().getLocation();
        WorkspaceResponse actual = result.getResponseBody();


        assertThat(location.getPath()).isEqualTo("/api/workspaces/1");
        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.name()).isEqualTo(name);
    }

    @Test
    void POST_shouldReturnBadRequest_whenNameEmpty() {
        WorkspaceRequest invalidRequest = WorkspaceRequest.builder()
                .name("")
                .build();

        ValidationProblemDetail actual = restTestClient.post().uri("/api/workspaces")
                .body(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ValidationProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Validation failed");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/workspaces"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(actual.getErrors().get("name")).contains("Workspace name length must be 1-20 characters");
    }

    @Test
    void POST_shouldReturnBadRequest_whenNameTooLong() {
        WorkspaceRequest invalidRequest = WorkspaceRequest.builder()
                .name("The workspace with a name that is a bit too long")
                .build();

        ValidationProblemDetail actual = restTestClient.post().uri("/api/workspaces")
                .body(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ValidationProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Validation failed");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/workspaces"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(actual.getErrors().get("name")).contains("Workspace name length must be 1-20 characters");
    }

    @Test
    void POST_shouldReturnBadRequest_whenNameNull() {
        WorkspaceRequest invalidRequest = WorkspaceRequest.builder().build();

        ValidationProblemDetail actual = restTestClient.post().uri("/api/workspaces")
                .body(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ValidationProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Validation failed");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/workspaces"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(actual.getErrors().get("name")).contains("Workspace must have a name");
    }

    @Test
    void PATCH_shouldReturnBadRequest_whenNameNull() {
        WorkspaceRequest invalidRequest = WorkspaceRequest.builder().build();

        ValidationProblemDetail actual = restTestClient.patch().uri("/api/workspaces/1")
                .body(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ValidationProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Validation failed");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/workspaces/1"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(actual.getErrors().get("name")).contains("Workspace must have a name");
    }

    @Test
    void PATCH_shouldReturnOk_whenValidIdAndRequest() {
        Instant createdAt = LocalDateTime.of(2020, 1, 20, 14, 30).toInstant(ZoneOffset.UTC);
        long id = 1L;
        WorkspaceResponse current = WorkspaceResponse.builder()
                .id(id)
                .name("oldWorkspace")
                .createdAt(createdAt)
                .updatedAt(createdAt)
                .build();
        String updatedName = "updatedWorkspace";
        WorkspaceRequest updateRequest = WorkspaceRequest.builder()
                .name(updatedName)
                .build();
        WorkspaceResponse updated = WorkspaceResponse.builder()
                .name(updatedName)
                .createdAt(createdAt)
                .updatedAt(LocalDateTime.of(2020, 1, 20, 14, 40).toInstant(ZoneOffset.UTC))
                .build();
        when(workspaceService.getWorkspaceById(id)).thenReturn(current);
        when(workspaceService.updateWorkspace(id, updateRequest)).thenReturn(updated);

        WorkspaceResponse actual = restTestClient.patch().uri("/api/workspaces/{id}", id)
                .body(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WorkspaceResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.name()).isEqualTo(updatedName);
        assertThat(actual.createdAt()).isEqualTo(createdAt); // createdAt should not change
        assertThat(actual.updatedAt()).isAfter(createdAt);   // updatedAt should be later than initial creation
    }

}
