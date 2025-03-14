package com.group3.sp25hsf302group3se1889vj.entity;

import com.group3.sp25hsf302group3se1889vj.enums.PermissionType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "permissions")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(50)")
    private PermissionType name;

}
