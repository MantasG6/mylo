package io.github.mantasg6.mylo.domain.goal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

import io.github.mantasg6.mylo.core.exception.ValidationProblemDetail;

@WebMvcTest(GoalController.class)
@AutoConfigureRestTestClient
public class GoalControllerTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private GoalService goalService;

    @Test
    void POST_shouldReturnCreated_whenValidRequest() {
        Long id = 1L;
        String name = "Goal #1";
        GoalRequest request = GoalRequest.builder()
                .name(name)
                .build();
        GoalResponse expected = GoalResponse.builder()
                .id(id)
                .name(name)
                .build();
        when(goalService.createGoal(request)).thenReturn(expected);

        EntityExchangeResult<GoalResponse> result = restTestClient.post().uri("/api/goals")
                .body(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(GoalResponse.class)
                .returnResult();
        URI location = result.getResponseHeaders().getLocation();
        GoalResponse actual = result.getResponseBody();

        assertThat(location.getPath()).isEqualTo("/api/goals/1");
        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.name()).isEqualTo(name);
    }

    @Test
    void POST_shouldReturnBadRequest_whenNameLongerThan25Characters() {
        GoalRequest invalidRequest = GoalRequest.builder()
                .name("Goal that has a name longer than 25 characters")
                .build();
        
        ValidationProblemDetail actual = restTestClient.post().uri("/api/goals")
                .body(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ValidationProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Validation failed");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/goals"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(actual.getErrors()).containsEntry("name", "Goal name must be 1-25 characters");
    }
}
