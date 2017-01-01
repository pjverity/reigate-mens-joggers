package uk.co.vhome.rmj.site.form.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

/**
 * Annotation placed on {@link uk.co.vhome.rmj.site.world.UserRegistrationFormObject} to validate
 * the submitted form inputs
 */
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserRegistrationValidator.class)
@ReportAsSingleViolation
public @interface UserRegistrationValid
{
	String message() default "User registration details are invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	static @interface List {
		UserRegistrationValid[] value();
	}
}
