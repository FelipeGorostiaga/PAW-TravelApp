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
@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ TYPE, ANNOTATION_TYPE })
public @interface FieldMatch {
    String message() default "{ar.edu.itba.paw.webapp.form.annotation.FieldMatch.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

        /**
         * @return The first field
         */
    String first();

        /**
         * @return The second field
         */
    String second();

        /**
         * Defines several <code>@FieldMatch</code> annotations on the same element
         *
         * @see FieldMatch
         */
    @Target({ TYPE, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FieldMatch[] value();
    }
}

