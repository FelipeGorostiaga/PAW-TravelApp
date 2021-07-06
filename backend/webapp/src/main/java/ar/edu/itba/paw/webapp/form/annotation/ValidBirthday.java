package ar.edu.itba.paw.webapp.form.annotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = BirthdayValidator.class)
@Target({TYPE, ANNOTATION_TYPE})
public @interface ValidBirthday {

    String message() default "{ar.edu.itba.paw.webapp.form.annotation.ValidBirthday.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
