package io.github.mantasg6.mylo.domain.workspace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = WorkspacePeriodValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidWorkspacePeriod {
    String message() default "The start of the period cannot be after the end of the period";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
