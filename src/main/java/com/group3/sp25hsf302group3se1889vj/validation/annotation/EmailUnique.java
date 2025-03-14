package com.group3.sp25hsf302group3se1889vj.validation.annotation;

import com.group3.sp25hsf302group3se1889vj.validation.validator.EmailUniqueValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailUniqueValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailUnique {
    String message() default "Email đã tồn tại";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
