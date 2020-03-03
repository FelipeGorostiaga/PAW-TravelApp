package ar.edu.itba.paw.webapp.form.annotation;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldMatchValidator.class);

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(value, secondFieldName);

            boolean matches = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);

            if (!matches) {
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addNode(secondFieldName).addConstraintViolation().disableDefaultConstraintViolation();
            }
            return matches;
        } catch (final Exception e) {
            LOGGER.error("Failed to read property on object {}: {}", value, e.getMessage());
        }

        return true;
    }
}