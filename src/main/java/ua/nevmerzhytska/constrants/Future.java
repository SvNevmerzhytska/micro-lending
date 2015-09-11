package ua.nevmerzhytska.constrants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureValidator.class)
@Documented
public @interface Future {
    String message() default "time should be in a future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
