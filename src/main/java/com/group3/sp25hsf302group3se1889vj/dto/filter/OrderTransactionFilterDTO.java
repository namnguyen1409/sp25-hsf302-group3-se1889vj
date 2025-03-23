package com.group3.sp25hsf302group3se1889vj.dto.filter;

import com.group3.sp25hsf302group3se1889vj.enums.OrderTransactionStatus;
import com.group3.sp25hsf302group3se1889vj.enums.OrderTransactionType;
import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTransactionFilterDTO extends BaseFilterDTO {
    // TODO: Add fields here
    private Long orderId;

    private String transactionId;

    private BigDecimal amountFrom;
    private BigDecimal amountTo;

    private OrderTransactionStatus status;

    private String vnPayResponseCode;

    private OrderTransactionType type;
}
