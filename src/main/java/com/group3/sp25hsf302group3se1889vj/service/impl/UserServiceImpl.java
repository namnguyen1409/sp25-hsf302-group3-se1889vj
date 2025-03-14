package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.repository.UserRepository;
import com.group3.sp25hsf302group3se1889vj.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean existsByEmail(String value) {
        return userRepository.existsByEmail(value);
    }

    @Override
    public boolean existsByPhone(String value) {
        return userRepository.existsByPhone(value);
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }
}
