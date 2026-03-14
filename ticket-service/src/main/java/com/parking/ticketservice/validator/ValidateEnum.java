package com.parking.ticketservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidateEnum {
    Class<? extends Enum<?>> enumClass();
    String message() default "Value is not part of the accepted list";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
