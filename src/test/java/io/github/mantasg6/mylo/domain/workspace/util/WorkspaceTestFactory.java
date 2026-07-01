package io.github.mantasg6.mylo.domain.workspace.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    public static final LocalDate DEFAULT_PERIOD_FROM = LocalDate
            .of(2020, 01, 01);
    public static final LocalDate DEFAULT_PERIOD_TO = LocalDate
            .of(2020, 02, 01);
    public static final LocalDateTime DEFAULT_CREATED_AT = LocalDateTime
            .of(2026, 06, 30, 21, 02);
    public static final LocalDateTime DEFAULT_UPDATED_AT = LocalDateTime
            .of(2026, 06, 30, 21, 02);

    public static final String UPDATED_NAME = "updatedWorkspace";
    public static final LocalDate UPDATED_PERIOD_FROM = LocalDate
            .of(2020, 01, 02);
    public static final LocalDate UPDATED_PERIOD_TO = LocalDate
            .of(2020, 02, 03);
    public static final LocalDateTime NEW_UPDATED_AT = LocalDateTime
            .of(2026, 07, 01, 20, 35);
    
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
     * Builds a fully updated {@link WorkspaceResponse}
     *
     * @return {@link WorkspaceResponse} with all fields updated.
     */
    public static WorkspaceResponse updateWorkspaceResponse() {
        return WorkspaceResponse.builder()
                .name(UPDATED_NAME)
                .periodFrom(UPDATED_PERIOD_FROM)
                .periodTo(UPDATED_PERIOD_TO)
                .build();
    }

}
