package io.github.mantasg6.mylo.domain.workspace.util;

import static io.github.mantasg6.mylo.domain.workspace.WorkspaceControllerTest.ENDPOINT_WITH_INVALID_ID;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.ResultActions;

import io.github.mantasg6.mylo.domain.workspace.WorkspaceErrorMessage;
import io.github.mantasg6.mylo.domain.workspace.WorkspaceRequest;
import io.github.mantasg6.mylo.domain.workspace.WorkspaceResponse;

/**
 * Sample data for workspace management testing
 *
 */
public final class WorkspaceTestFactory {

    public static final Long VALID_ID = 1L;
    public static final Long INVALID_ID = -1L;

    public static final String DEFAULT_NAME = "defaultWorkspace";
    public static final LocalDate DEFAULT_PERIOD_FROM = LocalDate.of(2020, 01, 01);
    public static final LocalDate DEFAULT_PERIOD_TO = LocalDate.of(2020, 02, 01);
    public static final Instant DEFAULT_CREATED_AT = Instant.parse("2026-06-30T21:02:00Z");
    public static final Instant DEFAULT_UPDATED_AT = Instant.parse("2026-06-30T21:02:00Z");

    public static final String UPDATED_NAME = "updatedWorkspace";
    public static final LocalDate UPDATED_PERIOD_FROM = LocalDate.of(2020, 01, 02);
    public static final LocalDate UPDATED_PERIOD_TO = LocalDate.of(2020, 02, 03);
    public static final Instant NEW_UPDATED_AT = Instant.parse("2026-07-01T21:50:00Z");
    
    /**
     * Private constructor. Utility class cannot be instantiated.
     *
     * @throws UnsupportedOperationException if tried to instantiate.
     */
    private WorkspaceTestFactory() {
        throw new UnsupportedOperationException(
            "Utility class cannot be instantiated"
        );
    }

    /**
     * Builds a default workspace request sample.
     *
     * @return {@link WorkspaceRequest} Default workspace request sample.
     */
    public static WorkspaceRequest defaultWorkspaceRequest() {
        return WorkspaceRequest.builder()
                .name(DEFAULT_NAME)
                .periodFrom(DEFAULT_PERIOD_FROM)
                .periodTo(DEFAULT_PERIOD_TO)
                .build();
    }

    /**
     * Builds a default workspace response sample.
     *
     * @return {@link WorkspaceResponse} Default workspace response sample.
     */
    public static WorkspaceResponse defaultWorkspaceResponse() {
        return WorkspaceResponse.builder()
                .name(DEFAULT_NAME)
                .periodFrom(DEFAULT_PERIOD_FROM)
                .periodTo(DEFAULT_PERIOD_TO)
                .createdAt(DEFAULT_CREATED_AT)
                .updatedAt(DEFAULT_UPDATED_AT)
                .build();

    }

    /**
     * Builds a workspace request to update name and period_to fields.
     *
     * @return {@link WorkspaceRequest} to update name and period_to fields.
     */
    public static WorkspaceRequest partialUpdateWorkspaceRequest() {
        return WorkspaceRequest.builder()
                .name(UPDATED_NAME)
                .periodTo(UPDATED_PERIOD_TO)
                .build();
    }

    /**
     * Builds a partially updated {@link WorkspaceResponse}.
     *
     * @return {@link WorkspaceResponse} with updated name and period_to.
     */
    public static WorkspaceResponse partialUpdateWorkspaceResponse() {
        return WorkspaceResponse.builder()
                .name(UPDATED_NAME)
                .periodFrom(DEFAULT_PERIOD_FROM)
                .periodTo(UPDATED_PERIOD_TO)
                .createdAt(DEFAULT_CREATED_AT)
                .updatedAt(NEW_UPDATED_AT)
                .build();
    }

    /**
     * Builds a workspace request to update all fields.
     *
     * @return {@link WorkspaceRequest} to update all workspace fields.
     */
    public static WorkspaceRequest updateWorkspaceRequest() {
        return WorkspaceRequest.builder()
                .name(UPDATED_NAME)
                .periodFrom(UPDATED_PERIOD_FROM)
                .periodTo(UPDATED_PERIOD_TO)
                .build();
    }

    /**
     * Builds a fully updated {@link WorkspaceResponse}.
     *
     * @return {@link WorkspaceResponse} with all fields updated.
     */
    public static WorkspaceResponse updateWorkspaceResponse() {
        return WorkspaceResponse.builder()
                .name(UPDATED_NAME)
                .periodFrom(UPDATED_PERIOD_FROM)
                .periodTo(UPDATED_PERIOD_TO)
                .createdAt(DEFAULT_CREATED_AT)
                .updatedAt(NEW_UPDATED_AT)
                .build();
    }


    /**
     * Assert that the json result is default {@link WorkspaceResponse}.
     *
     * @param result Json result.
     * @return {@link ResultActions} payload equals default {@link WorkspaceResponse}.
     * @throws Exception If any of the fields don't match.
     */
    public static ResultActions assertDefault(ResultActions result) throws Exception {
        return result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.periodFrom").value(DEFAULT_PERIOD_FROM.toString()))
                .andExpect(jsonPath("$.periodTo").value(DEFAULT_PERIOD_TO.toString()))
                .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
                .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    /**
     * Asster that the json result is partially updated {@link WorkspaceResponse}.
     *
     * @param result Json result.
     * @return {@link ResultActions} payload equals partially updated {@link WorkspaceResponse}.
     * @throws Exception If any of the fields don't match.
     */
    public static ResultActions assertPartiallyUpdated(ResultActions result) throws Exception {
        return result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.periodFrom").value(DEFAULT_PERIOD_FROM.toString()))
                .andExpect(jsonPath("$.periodTo").value(UPDATED_PERIOD_TO.toString()))
                .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
                .andExpect(jsonPath("$.updatedAt").value(NEW_UPDATED_AT.toString()));
    }

    /**
     * Assert a request with invalid id returns a Not Found {@link ProblemDetail} response
     *
     * @param result Json result.
     * @return {@link ResultActions} payload equals Not Found {@link ProblemDetail} response.
     * @throws Exception If any of the assertions fail.
     */
    public static ResultActions assertNotFound(ResultActions result) throws Exception {
        return result
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(WorkspaceErrorMessage.notFound(INVALID_ID)))
                .andExpect(jsonPath("$.instance").value(ENDPOINT_WITH_INVALID_ID))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.title").value(HttpStatus.NOT_FOUND.getReasonPhrase()));
    }

}
