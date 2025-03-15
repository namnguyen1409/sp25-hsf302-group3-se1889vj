package com.group3.sp25hsf302group3se1889vj.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_addresses")
public class CustomerAddress extends BaseEntity {

    private boolean isDefault = false;

    @Column(nullable = false, columnDefinition = "NVARCHAR(50)")
    private String fullName;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String address;

    @Column(nullable = false, columnDefinition = "NVARCHAR(20)")
    private String phone;
}
