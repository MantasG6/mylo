package io.github.mantasg6.mylo.domain.workspace;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom workspace period validator to make sure period start is not after period end.
 *
 */
public class WorkspacePeriodValidator implements ConstraintValidator<ValidWorkspacePeriod, WorkspaceRequest> {

	@Override
	public boolean isValid(WorkspaceRequest request, ConstraintValidatorContext context) {
        if (request.periodStart() == null || request.periodEnd() == null) {
            return true;
        }
        return !request.periodStart().isAfter(request.periodEnd());
	}

}
