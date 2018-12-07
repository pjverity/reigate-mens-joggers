package uk.co.vhome.clubbed.web.controllers.userregistration;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

/**
 * Annotation placed on {@link UserRegistrationFormObject} to validate
 * the submitted form inputs
 */
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserRegistrationValidator.class)
@ReportAsSingleViolation
public @interface UserRegistrationValid
{
	String message() default "{validation.constraint.UserRegistrationValid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		UserRegistrationValid[] value();
	}
}
