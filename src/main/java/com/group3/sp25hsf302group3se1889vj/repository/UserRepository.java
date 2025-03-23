package com.group3.sp25hsf302group3se1889vj.repository;


import com.group3.sp25hsf302group3se1889vj.entity.Permission;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<com.group3.sp25hsf302group3se1889vj.entity.User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    Page<User> findByUsernameContaining(String username, Pageable pageable);
    Page<User> findUserByRole(RoleType role, Pageable pageable);

    Page<User> findAll(Pageable pageable);
    Optional<User> findById(Long id);

    List<User> findByPermissionsContains(Permission permission);

    long countByRole(RoleType role);

    Long countByGender(Boolean gender);

    @Query("SELECT YEAR(u.createdAt), MONTH(u.createdAt), COUNT(u) " +
            "FROM User u " +
            "GROUP BY YEAR(u.createdAt), MONTH(u.createdAt) " +
            "ORDER BY YEAR(u.createdAt) DESC, MONTH(u.createdAt) DESC")
    List<Object[]> countNewUsersByMonth();

    Long countByIsActive(Boolean aTrue);
}
