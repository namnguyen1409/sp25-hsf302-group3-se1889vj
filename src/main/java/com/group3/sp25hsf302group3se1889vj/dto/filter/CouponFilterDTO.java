package com.group3.sp25hsf302group3se1889vj.dto.filter;

import com.group3.sp25hsf302group3se1889vj.enums.CouponType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponFilterDTO extends BaseFilterDTO {
    private String code;
    private String description;
    private CouponType type;
    private BigDecimal valueFrom;
    private BigDecimal valueTo;
    private BigDecimal minOrderValueFrom;
    private BigDecimal minOrderValueTo;
    private Integer maxDiscountFrom;
    private Integer maxDiscountTo;
    private Integer maxUsageFrom;
    private Integer maxUsageTo;
    private Integer usageCountFrom;
    private Integer usageCountTo;
    private Integer maxUsagePerUserFrom;
    private Integer maxUsagePerUserTo;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTo;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTo;
    private Boolean isDeleted;
}
