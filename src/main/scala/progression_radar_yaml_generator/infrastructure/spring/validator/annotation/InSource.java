package progression_radar_yaml_generator.infrastructure.spring.validator.annotation;

import progression_radar_yaml_generator.infrastructure.spring.validator.InSourceConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = InSourceConstraintValidator.class)
public @interface InSource {

    String message() default "{progression_radar_yaml_generator.spring.validator.In.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}