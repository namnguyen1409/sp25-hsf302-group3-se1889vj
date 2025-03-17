package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.enums.OrderStatus;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusHistoryDTO extends BaseDTO {
    // TODO: Add fields here
    private Long orderId;
    private OrderStatus orderStatus;
}
