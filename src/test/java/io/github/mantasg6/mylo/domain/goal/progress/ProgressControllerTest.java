package io.github.mantasg6.mylo.domain.goal.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import io.github.mantasg6.mylo.core.exception.ValidationProblemDetail;
import io.github.mantasg6.mylo.domain.goal.GoalNotFoundException;

@WebMvcTest(ProgressController.class)
@AutoConfigureRestTestClient
public class ProgressControllerTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private ProgressService progressService;

    @Test
    void GET_shouldReturnOkAndCorrectProgress_whenProgressFound() {
        Long goalId = 1L;
        LocalDate effectiveDate = LocalDate.of(2001, 9, 1);
        ProgressId id = new ProgressId(goalId, effectiveDate);
        ProgressResponse response = ProgressResponse.builder()
                .amount(new BigDecimal("30.1")).build();
        when(progressService.getProgress(id)).thenReturn(response);

        ProgressResponse actual = restTestClient.get()
                .uri("/api/goals/1/progress/2001-09-01")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProgressResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.amount()).isEqualTo(new BigDecimal("30.1"));
    }

    @Test
    void GET_shouldReturnNotFound_whenGoalNotFound() {
        Long goalId = 999L;
        LocalDate effectiveDate = LocalDate.of(2002, 1, 10);
        ProgressId id = new ProgressId(goalId, effectiveDate);
        when(progressService.getProgress(id)).thenThrow(new GoalNotFoundException(goalId));

        ProblemDetail actual = restTestClient.get()
                .uri("/api/goals/999/progress/2002-01-10")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Goal with id 999 not found!");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/goals/999/progress/2002-01-10"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @Test
    void PUT_shouldReturnOkAndNewProgress_whenValidRequest() {
        Long goalId = 1L;
        LocalDate effectiveDate = LocalDate.of(2002, 1, 10);
        ProgressId id = new ProgressId(goalId, effectiveDate);
        BigDecimal amount = new BigDecimal("1");
        ProgressRequest request = ProgressRequest.builder()
                .amount(amount).build();
        ProgressResponse response = ProgressResponse.builder()
                .amount(amount).build();
        when(progressService.updateProgress(id, request)).thenReturn(response);

        ProgressResponse actual = restTestClient.put()
                .uri("/api/goals/1/progress/2002-01-10")
                .body(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProgressResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.amount()).isEqualTo(amount);
    }

    @Test
    void PUT_shouldReturnNotFound_whenGoalNotFound() {
        Long goalId = 9999L;
        LocalDate effectiveDate = LocalDate.of(2011, 7, 29);
        ProgressId id = new ProgressId(goalId, effectiveDate);
        ProgressRequest request = ProgressRequest.builder()
                .amount(new BigDecimal("111.2")).build();
        when(progressService.updateProgress(id, request)).thenThrow(new GoalNotFoundException(goalId));

        ProblemDetail actual = restTestClient.put()
                .uri("/api/goals/9999/progress/2011-07-29")
                .body(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Goal with id 9999 not found!");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/goals/9999/progress/2011-07-29"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @Test
    void PUT_shouldReturnBadRequest_whenNoAmount() {
        ProgressRequest invalidRequest = ProgressRequest.builder().build();

        ValidationProblemDetail actual = restTestClient.put()
                .uri("/api/goals/1/progress/2010-09-08")
                .body(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ValidationProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Validation failed");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/goals/1/progress/2010-09-08"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(actual.getErrors().get("amount")).contains("Amount is required");
    }

    @Test
    void PUT_shouldReturnBadRequest_whenAmountTooBig() {
        ProgressRequest invalidRequest = ProgressRequest.builder()
                .amount(new BigDecimal("1000000000")).build();

        ValidationProblemDetail actual = restTestClient.put()
                .uri("/api/goals/1/progress/2026-08-09")
                .body(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ValidationProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Validation failed");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/goals/1/progress/2026-08-09"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(actual.getErrors().get("amount"))
                .contains("Amount must be up to 1 billion with 3 decimal places at most");
    }

    @Test
    void PUT_shouldReturnBadRequest_whenAmountMoreThan3DecimalPlaces() {
        ProgressRequest invalidRequest = ProgressRequest.builder()
                .amount(new BigDecimal("199.1847"))
                .build();

        ValidationProblemDetail actual = restTestClient.put()
                .uri("/api/goals/1/progress/2020-01-23")
                .body(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ValidationProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Validation failed");
        assertThat(actual.getInstance()).isEqualTo(URI.create("/api/goals/1/progress/2020-01-23"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(actual.getErrors().get("amount"))
                .contains("Amount must be up to 1 billion with 3 decimal places at most");
    }


    @Test
    void PUT_shouldReturnOk_whenAmountAlmostTooBig() {
        Long goalId = 1L;
        LocalDate effectiveDate = LocalDate.of(2003, 4, 12);
        ProgressId id = new ProgressId(goalId, effectiveDate);
        BigDecimal amount = new BigDecimal("999999999.999");
        ProgressRequest request = ProgressRequest.builder()
                .amount(amount).build();
        ProgressResponse response = ProgressResponse.builder()
                .amount(amount).build();
        when(progressService.updateProgress(id, request)).thenReturn(response);

        ProgressResponse actual = restTestClient.put()
                .uri("/api/goals/1/progress/2003-04-12")
                .body(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProgressResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.amount()).isEqualTo(amount);
    }

    @Test
    void PUT_shouldReturnBadRequest_whenEffectiveDateInFuture() {
        LocalDate effectiveDate = LocalDate.now().plusDays(1);
        ProgressRequest request = ProgressRequest.builder()
                .amount(new BigDecimal("101")).build();

        ValidationProblemDetail actual = restTestClient.put()
                .uri("/api/goals/1/progress/{effectiveDate}", effectiveDate)
                .body(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ValidationProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Validation failed");
        assertThat(actual.getInstance()).isEqualTo(
                URI.create(String.format("/api/goals/1/progress/%s", effectiveDate)));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(actual.getErrors().get("effectiveDate"))
                .contains("Effective Date cannot be in the future");
    }

    @Test
    void PUT_shouldReturnBadRequest_whenEffectiveDateFormatInvalid() {
        ProgressRequest request = ProgressRequest.builder()
                .amount(new BigDecimal("34")).build();

        ValidationProblemDetail actual = restTestClient.put()
                .uri("/api/goals/1/progress/202020-01-23")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ValidationProblemDetail.class)
                .returnResult()
                .getResponseBody();

        assertThat(actual.getDetail()).isEqualTo("Validation failed");
        assertThat(actual.getInstance()).isEqualTo(
                URI.create("/api/goals/1/progress/202020-01-23"));
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(actual.getTitle()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(actual.getErrors().get("effectiveDate"))
                .contains("Invalid value '202020-01-23'. Expected type: LocalDate");
    }
}
