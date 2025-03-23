package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.enums.OrderTransactionStatus;
import com.group3.sp25hsf302group3se1889vj.enums.OrderTransactionType;
import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTransactionDTO extends BaseDTO {
    @FieldMetadata(title = "Mã đơn hàng", cssClass = "order-id")
    private Long orderId;

    @FieldMetadata(title = "Mã giao dịch")
    private String transactionId;

    @FieldMetadata(title = "Tổng tiền", cssClass = "price")
    private BigDecimal amount;

    @FieldMetadata(title = "Trạng thái", cssClass = "order-transaction-status")
    private OrderTransactionStatus status;

    @FieldMetadata(title = "Mã giao dịch VNPay")
    private String vnPayResponseCode;

    @FieldMetadata(title = "Loại giao dịch", cssClass = "order-transaction-type")
    private OrderTransactionType type;

}
