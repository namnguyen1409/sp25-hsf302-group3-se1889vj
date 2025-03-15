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
    // TODO: Add fields here
    @FieldMetadata(title = "Mã đơn hàng")
    private Long id;

    @FieldMetadata(title = "Mã giao dịch")
    private String transactionId;

    @FieldMetadata(title = "Tổng tiền")
    private BigDecimal amount;

    @FieldMetadata(title = "Trạng thái")
    private OrderTransactionStatus status;

    private String vnPayResponseCode;

    @FieldMetadata(title = "Loại giao dịch")
    private OrderTransactionType type;

}
