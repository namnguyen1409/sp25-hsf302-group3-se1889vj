package com.group3.sp25hsf302group3se1889vj.validation.annotation;

import com.group3.sp25hsf302group3se1889vj.validation.validator.FieldsEqualValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = FieldsEqualValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsEqual {

    String message() default "Fields not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field1();

    String field2();

}
