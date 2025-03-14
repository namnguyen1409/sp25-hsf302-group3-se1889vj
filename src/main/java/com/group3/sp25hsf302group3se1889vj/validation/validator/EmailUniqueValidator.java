package com.group3.sp25hsf302group3se1889vj.validation.validator;

import com.group3.sp25hsf302group3se1889vj.service.UserService;
import com.group3.sp25hsf302group3se1889vj.validation.annotation.EmailUnique;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {

    private UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isExist = userService.existsByEmail(value);
        return !isExist;
    }
}
