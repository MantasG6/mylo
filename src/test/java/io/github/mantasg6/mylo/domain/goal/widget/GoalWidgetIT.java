package io.github.mantasg6.mylo.domain.goal.widget;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.URI;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

import io.github.mantasg6.mylo.core.BaseIT;
import io.github.mantasg6.mylo.domain.goal.Goal;
import io.github.mantasg6.mylo.domain.goal.GoalRepository;
import io.github.mantasg6.mylo.domain.goal.GoalResponse;
import io.github.mantasg6.mylo.domain.widget.Widget;
import io.github.mantasg6.mylo.domain.widget.WidgetRepository;
import io.github.mantasg6.mylo.domain.widget.WidgetRequest;
import io.github.mantasg6.mylo.domain.widget.WidgetResponse;
import io.github.mantasg6.mylo.domain.widget.WidgetType;
import io.github.mantasg6.mylo.domain.workspace.Workspace;
import io.github.mantasg6.mylo.domain.workspace.WorkspaceRepository;

@AutoConfigureRestTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GoalWidgetIT extends BaseIT {

    @Autowired private RestTestClient restTestClient;
    @Autowired private WorkspaceRepository workspaceRepository;
    @Autowired private WidgetRepository widgetRepository;
    @Autowired private GoalWidgetRepository goalWidgetRepository;
    @Autowired private GoalRepository goalRepository;

    private Workspace workspace;
    private Goal goal;

    @BeforeAll
    void setUpWorkspace() {
        workspace = new Workspace();
        workspace.setName("Test Workspace");
        workspace = workspaceRepository.save(workspace);

        goal = new Goal();
        goal.setName("Test Goal");
        goal.setTarget(new BigDecimal("19.014"));
        goal = goalRepository.save(goal);
    }

    @AfterAll
    void cleanUpWorkspace() {
        workspaceRepository.deleteAll();
        goalRepository.deleteAll();
    }

    @AfterEach
    void cleanWidgets() {
        widgetRepository.deleteAll();
        goalWidgetRepository.deleteAll();
    }

    @Test
    void GET_shouldReturnOk_whenValidId() {
        Widget widget = widgetRepository.save(new Widget(workspace, 1, WidgetType.GOAL));
        goalWidgetRepository.save(new GoalWidget(widget, goal));

        WidgetResponse<GoalResponse> actual = restTestClient.get().uri("/api/widgets/{id}", widget.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<WidgetResponse<GoalResponse>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(actual.id()).isEqualTo(widget.getId());
        assertThat(actual.position()).isEqualTo(1);
        assertThat(actual.content().id()).isEqualTo(goal.getId());
        assertThat(actual.content().name()).isEqualTo("Test Goal");
    }

    @Test
    void POST_shouldReturnCreated_whenValidRequest() {
        WidgetRequest request = WidgetRequest.builder()
                .workspaceId(workspace.getId())
                .type(WidgetType.GOAL)
                .position(2)
                .contentId(goal.getId())
                .build();

        EntityExchangeResult<WidgetResponse<GoalResponse>> result = restTestClient.post().uri("/api/widgets")
                .body(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<WidgetResponse<GoalResponse>>() {})
                .returnResult();
        URI location = result.getResponseHeaders().getLocation();
        WidgetResponse<GoalResponse> actual = result.getResponseBody();


        assertThat(location.getPath()).isEqualTo("/api/widgets/%s".formatted(actual.id()));
        assertThat(actual.workspaceId()).isEqualTo(workspace.getId());
        assertThat(actual.position()).isEqualTo(2);
        assertThat(actual.type()).isEqualTo(WidgetType.GOAL);
        assertThat(actual.content().id()).isEqualTo(goal.getId());
        assertThat(actual.content().name()).isEqualTo(goal.getName());
        assertThat(actual.content().target()).isEqualTo(goal.getTarget());
    }

    @Test
    void POST_shouldReturnNotFoundAndRollback_whenGoalNotFound() {
        WidgetRequest request = WidgetRequest.builder()
                .workspaceId(workspace.getId())
                .type(WidgetType.GOAL)
                .position(2)
                .contentId(99L)
                .build();

        ProblemDetail actual = restTestClient.post().uri("/api/widgets")
                .body(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(widgetRepository.count()).isZero();
        assertThat(actual.getDetail()).isEqualTo("Goal with id 99 not found!");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/widgets"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @Test
    void PATCH_shouldReturnNotFoundAndRollback_whenNewGoalNotFound() {
        Widget widget = widgetRepository.save(new Widget(workspace, 1, WidgetType.GOAL));
        goalWidgetRepository.save(new GoalWidget(widget, goal));
        WidgetRequest request = WidgetRequest.builder()
                .workspaceId(workspace.getId())
                .position(2)
                .contentId(99L)
                .build();

        ProblemDetail actual = restTestClient.patch().uri("/api/widgets/{id}", widget.getId())
                .body(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();

        // Make sure widget changes were rolled back
        Widget widgetAfter = widgetRepository.findById(widget.getId()).orElse(null);
        assertThat(widgetAfter.getId()).isEqualTo(widget.getId());
        assertThat(widgetAfter.getType()).isEqualTo(widget.getType());
        assertThat(widgetAfter.getWorkspace().getId()).isEqualTo(widget.getWorkspace().getId());
        assertThat(widgetAfter.getPosition()).isEqualTo(widget.getPosition());
        // Make sure the correct error is shown to the user
        assertThat(actual.getDetail()).isEqualTo("Goal with id 99 not found!");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/widgets/%s".formatted(widget.getId())));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

}
