package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.enums.CouponType;
import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO extends BaseDTO {
    // TODO: Add fields here
    @FieldMetadata(title = "Mã giảm giá")
    @NotBlank(message = "Mã giảm giá không được để trống")
    @Size(min = 1, max = 50, message = "Mã giảm giá phải từ 1 đến 50 ký tự")
    private String code;

    @FieldMetadata(title = "Mô tả")
    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 1, max = 255, message = "Mô tả phải từ 1 đến 255 ký tự")
    private String description;

    @FieldMetadata(title = "Loại", cssClass = "couponType")
    @NotNull(message = "Loại không được để trống")
    private CouponType type;

    @FieldMetadata(title = "Giá trị")
    @PositiveOrZero(message = "Giá trị mã giảm giá phải là số dương hoặc bằng 0")
    private BigDecimal value;

    @FieldMetadata(title = "Đơn hàng tối thiểu", cssClass = "price")
    @PositiveOrZero(message = "Đơn hàng tối thiểu phải là số dương hoặc bằng 0")
    private BigDecimal minOrderValue;

    @FieldMetadata(title = "Giảm tối đa", cssClass = "price")
    private BigDecimal maxDiscount;

    @FieldMetadata(title = "Số lượng mã")
    @PositiveOrZero(message = "Số lượng mã phải là số dương hoặc bằng 0")
    private Integer maxUsage;

    @FieldMetadata(title = "Số lần đã sử dụng")
    private Integer usageCount;

    @FieldMetadata(title = "Số lần sử dụng/khách")
    @PositiveOrZero(message = "Số lần sử dụng/khách phải là số dương hoặc bằng 0")
    private Integer maxUsagePerUser;

    @FieldMetadata(title = "Ngày bắt đầu", cssClass = "dateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDateTime startDate;

    @FieldMetadata(title = "Ngày kết thúc", cssClass = "dateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDateTime endDate;


    // methods validation
    public boolean isValid(BigDecimal orderValue) {
        return orderValue.compareTo(minOrderValue) >= 0
                && !LocalDateTime.now().isBefore(startDate)
                && !LocalDateTime.now().isAfter(endDate)
                && maxUsage > usageCount
                && !super.isDeleted();
    }

}
