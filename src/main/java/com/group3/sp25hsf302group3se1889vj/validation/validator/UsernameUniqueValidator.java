package com.group3.sp25hsf302group3se1889vj.validation.validator;

import com.group3.sp25hsf302group3se1889vj.service.UserService;
import com.group3.sp25hsf302group3se1889vj.validation.annotation.UsernameUnique;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsernameUniqueValidator implements ConstraintValidator<UsernameUnique, String> {

    private UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isExist = userService.isUsernameExist(value);
        return !isExist;
    }
}
