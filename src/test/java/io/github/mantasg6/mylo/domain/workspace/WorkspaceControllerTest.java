package io.github.mantasg6.mylo.domain.workspace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
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
    RestTestClient restTestClient;

    @MockitoBean
    private WorkspaceService workspaceService;

    @Test
    void shouldReturnAll_whenGetWithNoId() {
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
    void shouldReturnSingleWorkspace_whenGetWithId() {
        WorkspaceResponse expected = WorkspaceResponse.builder()
                .name("expectedWorkspace")
                .periodStart(LocalDate.of(2020, 06, 20))
                .periodEnd(LocalDate.of(2020, 06, 21))
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
        assertThat(response.periodStart()).isEqualTo(LocalDate.of(2020, 06, 20));
        assertThat(response.periodEnd()).isEqualTo(LocalDate.of(2020, 06, 21));
    }

    @Test
    void shouldReturnNotFound_whenGetWithInvalidId() {
        Long invalidId = 999L;
        when(workspaceService.getWorkspaceById(invalidId))
                .thenThrow(new WorkspaceNotFoundException(invalidId));

        ProblemDetail response = restTestClient.get().uri("/api/workspaces/999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(response.getDetail()).isEqualTo("Workspace with id 999 not found!");
        assertThat(response.getInstance()).isEqualTo(URI.create("/api/workspaces/999"));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @Test
    void shouldReturnCreated_whenPostWithValidRequest() {
        Long id = 1L;
        String name = "workspace";
        LocalDate periodStart = LocalDate.of(2025, 8, 20);
        LocalDate periodEnd = LocalDate.of(2025, 9, 20);
        WorkspaceRequest request = WorkspaceRequest.builder()
                .name(name)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .build();
        WorkspaceResponse expected = WorkspaceResponse.builder()
                .id(id)
                .name(name)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
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
        assertThat(actual.periodStart()).isEqualTo(periodStart);
        assertThat(actual.periodEnd()).isEqualTo(periodEnd);
    }

    @Test
    void shouldReturnBadRequest_whenPostWithInvalidRequest() {
        WorkspaceRequest invalidRequest = WorkspaceRequest.builder()
                .name("")
                .periodStart(null)
                .periodEnd(null)
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
        assertThat(actual.getErrors()).containsEntry("name", "Workspace name cannot be blank");
        assertThat(actual.getErrors()).containsEntry("periodStart", "The start of the period must be provided");
        assertThat(actual.getErrors()).containsEntry("periodEnd", "The end of the period must be provided");
    }

    @Test
    void shouldReturnBadRequest_whenPeriodStartIsAfterPeriodEnd() {
        WorkspaceRequest invalidRequest = WorkspaceRequest.builder()
                .name("invalidWorkspaceName")
                .periodStart(LocalDate.of(2000, 11, 3))
                .periodEnd(LocalDate.of(2000, 11, 1))
                .build();

        ValidationProblemDetail actual = restTestClient.post().uri("/api/workspaces")
                .body(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ValidationProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getErrors()).containsEntry("workspaceRequest",
                "The start of the period cannot be after the end of the period");
    }

    @Test
    void shouldReturnUpdated_whenPutWithValidIdAndRequest() {
        Instant createdAt = LocalDateTime.of(2020, 1, 20, 14, 30).toInstant(ZoneOffset.UTC);
        long id = 1L;
        WorkspaceResponse current = WorkspaceResponse.builder()
                .id(id)
                .name("oldWorkspace")
                .periodStart(LocalDate.of(2000, 9, 1))
                .periodEnd(LocalDate.of(2001, 9, 1))
                .createdAt(createdAt)
                .updatedAt(createdAt)
                .build();
        String updatedName = "updatedWorkspace";
        LocalDate updatedPeriodFrom = LocalDate.of(2020, 2, 1);
        LocalDate updatedPeriodTo = LocalDate.of(2021, 2, 1);
        WorkspaceRequest updateRequest = WorkspaceRequest.builder()
                .name(updatedName)
                .periodStart(updatedPeriodFrom)
                .periodEnd(updatedPeriodTo)
                .build();
        WorkspaceResponse updated = WorkspaceResponse.builder()
                .name(updatedName)
                .periodStart(updatedPeriodFrom)
                .periodEnd(updatedPeriodTo)
                .createdAt(createdAt)
                .updatedAt(LocalDateTime.of(2020, 1, 20, 14, 40).toInstant(ZoneOffset.UTC))
                .build();
        when(workspaceService.getWorkspaceById(id)).thenReturn(current);
        when(workspaceService.updateWorkspace(id, updateRequest)).thenReturn(updated);

        WorkspaceResponse actual = restTestClient.put().uri("/api/workspaces/{id}", id)
                .body(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WorkspaceResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.name()).isEqualTo(updatedName);
        assertThat(actual.periodStart()).isEqualTo(updatedPeriodFrom);
        assertThat(actual.periodEnd()).isEqualTo(updatedPeriodTo);
        assertThat(actual.createdAt()).isEqualTo(createdAt); // createdAt should not change
        assertThat(actual.updatedAt()).isAfter(createdAt);   // updatedAt should be later than initial creation
    }

    @Test
    void shouldReturnBadRequest_whenPeriodStartInFuture() {
        WorkspaceRequest workspaceRequest = WorkspaceRequest.builder()
                .name("Workspace")
                .periodStart(LocalDate.now().plusDays(1))
                .periodEnd(LocalDate.now().plusDays(1))
                .build();

        ValidationProblemDetail actual = restTestClient.post().uri("/api/workspaces")
        .body(workspaceRequest)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ValidationProblemDetail.class)
        .returnResult()
        .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Validation failed");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/workspaces"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(actual.getErrors()).containsEntry("periodStart", "Period start cannot be in the future");
    }
}
