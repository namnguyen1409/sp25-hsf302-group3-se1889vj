package com.group3.sp25hsf302group3se1889vj.entity;

import com.group3.sp25hsf302group3se1889vj.enums.CouponType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon extends BaseEntity {

    @Column(columnDefinition = "NVARCHAR(50)")
    private String code;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "NVARCHAR(20)")
    private CouponType type;

    private BigDecimal value;
    private BigDecimal minOrderValue;
    private Integer maxDiscount;
    private Integer maxUsage;
    private Integer usageCount;
    private Integer maxUsagePerUser;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
