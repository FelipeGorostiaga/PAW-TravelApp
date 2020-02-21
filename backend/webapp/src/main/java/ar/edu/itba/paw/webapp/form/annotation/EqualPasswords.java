package ar.edu.itba.paw.webapp.form.annotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EqualPasswordValidator.class})
public @interface EqualPasswords {

    String message() default "Passwords dont match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
