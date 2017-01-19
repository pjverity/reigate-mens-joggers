package uk.co.vhome.rmj.site.form.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

/**
 * Annotation placed on {@link uk.co.vhome.rmj.site.member.PasswordChangeFormObject} to validate
 * the submitted form inputs
 */
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordChangeValidator.class)
@ReportAsSingleViolation
public @interface PasswordChangeValid
{
	String message() default "{validation.constraint.PasswordChangeValid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	static @interface List {
		PasswordChangeValid[] value();
	}
}
