package com.group3.sp25hsf302group3se1889vj.validation.validator;

import com.group3.sp25hsf302group3se1889vj.service.UserService;
import com.group3.sp25hsf302group3se1889vj.validation.annotation.PhoneUnique;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PhoneUniqueValidator implements ConstraintValidator<PhoneUnique, String> {

    private UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isExist = userService.existsByPhone(value);
        return !isExist;
    }
}
