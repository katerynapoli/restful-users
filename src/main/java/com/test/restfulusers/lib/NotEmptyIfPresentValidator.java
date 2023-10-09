package com.test.restfulusers.lib;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEmptyIfPresentValidator implements ConstraintValidator<NotEmptyIfPresent, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value == null || !value.toString().trim().isEmpty();
    }
}
