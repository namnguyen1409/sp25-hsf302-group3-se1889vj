package com.group3.sp25hsf302group3se1889vj.dto.filter;

import com.group3.sp25hsf302group3se1889vj.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFilterDTO extends BaseFilterDTO {
    // TODO: Add fields here
    private Long id;
    private OrderStatus status;
    private BigDecimal totalPriceFrom;
    private BigDecimal totalPriceTo;
    private BigDecimal discountAmountFrom;
    private BigDecimal discountAmountTo;
    private BigDecimal finalPriceFrom;
    private BigDecimal finalPriceTo;
    private String note;
}
