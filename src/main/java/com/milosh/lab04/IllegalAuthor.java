package com.milosh.lab04;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IllegalAuthorValidator.class)
public @interface IllegalAuthor {
    String message() default "Niepoprawny format autora";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    //opcjonalnie własne parametry konfiguracyjne adnotacji
    String[] values() default {};
    boolean ignoreCase() default false;
}
