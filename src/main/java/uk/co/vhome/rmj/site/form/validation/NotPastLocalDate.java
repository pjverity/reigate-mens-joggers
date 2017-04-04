package uk.co.vhome.rmj.site.form.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = FutureDateValidator.class)
@Documented
public @interface NotPastLocalDate
{
	String message() default "{validation.constraint.PastLocalDate}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		NotPastLocalDate[] value();
	}
}
