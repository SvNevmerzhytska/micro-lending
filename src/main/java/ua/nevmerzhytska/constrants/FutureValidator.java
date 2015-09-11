package ua.nevmerzhytska.constrants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.Temporal;

public class FutureValidator implements ConstraintValidator<Future, Temporal> {
    @Override
    public void initialize(Future future) {

    }

    @Override
    public boolean isValid(Temporal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDate ld = LocalDate.from(value);
        if (ld.isAfter(LocalDate.now())) {
            return true;
        }
        return false;
    }
}
