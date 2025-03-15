package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.UserDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.UserFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.mapper.UserMapper;
import com.group3.sp25hsf302group3se1889vj.repository.UserRepository;
import com.group3.sp25hsf302group3se1889vj.service.UserService;
import com.group3.sp25hsf302group3se1889vj.specification.UserSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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

    @Override
    public Page<UserDTO> searchUsers(UserFilterDTO filter, Pageable pageable) {
        Specification<User> specification = UserSpecification.filterUser(filter);
        return userRepository.findAll(specification, pageable).map(userMapper::mapToUserDTO);
    }

    @Override
    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById((long) id);
        return userMapper.mapToUserDTO(user.get());
    }
}
