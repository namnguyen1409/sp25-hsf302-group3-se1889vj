package com.group3.sp25hsf302group3se1889vj.entity;

import com.group3.sp25hsf302group3se1889vj.enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity{

    @EqualsAndHashCode.Include
    @Column(name = "username", nullable = false, unique = true, columnDefinition = "nvarchar(50)")
    private String username;

    @EqualsAndHashCode.Include
    @Column(name = "password", nullable = false, columnDefinition = "nvarchar(100)")
    private String password;

    @EqualsAndHashCode.Include
    @Column(name = "first_name", nullable = false, columnDefinition = "nvarchar(50)")
    private String firstName;

    @EqualsAndHashCode.Include
    @Column(name = "last_name", nullable = false, columnDefinition = "nvarchar(50)")
    private String lastName;

    @EqualsAndHashCode.Include
    @Column(name = "email", nullable = false, unique = true, columnDefinition = "nvarchar(100)")
    private String email;

    @EqualsAndHashCode.Include
    @Column(name = "phone", nullable = false, unique = true, columnDefinition = "nvarchar(20)")
    private String phone;

    @EqualsAndHashCode.Include
    @Column(name = "gender", nullable = false)
    private Boolean gender;

    @EqualsAndHashCode.Include
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @EqualsAndHashCode.Include
    @Column(name = "address", nullable = false, columnDefinition = "nvarchar(255)")
    private String address;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = false;

    @Column(name = "is_locked", nullable = false)
    private boolean isLocked = false;

    @Column(name = "lock_reason", columnDefinition = "nvarchar(255)")
    private String lockReason;

    @Column(name = "avatar", columnDefinition = "nvarchar(255)")
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "nvarchar(20)")
    private RoleType role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
    
}
