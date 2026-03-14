package com.parking.ticketservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<ValidateEnum, String> {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValidateEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        boolean isValid = acceptedValues.contains(value.toUpperCase());

        if (!isValid) {
            // 1. Disable the default static message
            context.disableDefaultConstraintViolation();

            // 2. Build the dynamic string: "Invalid value. Allowed: [CAR, MOTORCYCLE]"
            String dynamicMessage = "Invalid value. Allowed values are: " + acceptedValues;

            // 3. Inject it into the context
            context.buildConstraintViolationWithTemplate(dynamicMessage)
                    .addConstraintViolation();
        }

        return isValid;
    }
}