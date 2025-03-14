package com.group3.sp25hsf302group3se1889vj.service;

public interface UserService {
    boolean existsByEmail(String value);

    boolean existsByPhone(String value);

    boolean isUsernameExist(String username);
}
