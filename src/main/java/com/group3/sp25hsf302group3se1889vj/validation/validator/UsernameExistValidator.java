package com.group3.sp25hsf302group3se1889vj.validation.validator;

import com.group3.sp25hsf302group3se1889vj.service.UserService;
import com.group3.sp25hsf302group3se1889vj.validation.annotation.UsernameExist;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsernameExistValidator implements ConstraintValidator<UsernameExist, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return userService.isUsernameExist(username);
    }
}
