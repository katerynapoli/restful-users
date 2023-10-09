package com.test.restfulusers.lib;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotEmptyIfPresentValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyIfPresent {
    String message() default "The updated field must not be empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
