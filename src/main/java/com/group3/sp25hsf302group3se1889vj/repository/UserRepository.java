package com.group3.sp25hsf302group3se1889vj.repository;


import com.group3.sp25hsf302group3se1889vj.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<com.group3.sp25hsf302group3se1889vj.entity.User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    Page<User> findByUsernameContaining(String username, Pageable pageable);
}
