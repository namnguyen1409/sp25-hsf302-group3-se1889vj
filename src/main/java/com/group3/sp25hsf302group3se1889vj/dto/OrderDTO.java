package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.entity.OrderItem;
import com.group3.sp25hsf302group3se1889vj.entity.OrderStatusHistory;
import com.group3.sp25hsf302group3se1889vj.enums.OrderStatus;
import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO extends BaseDTO {
    private Long addressId;
    private String couponCode;

    private CustomerAddressDTO customerAddress;

    @FieldMetadata(title = "Trạng thái đơn hàng")
    private OrderStatus status;

    @FieldMetadata(title = "Tổng tiền", cssClass = "price")
    private BigDecimal totalPrice;

    @FieldMetadata(title = "Giảm giá", cssClass = "price")
    private BigDecimal discountAmount;

    @FieldMetadata(title = "Phải trả", cssClass = "price")
    private BigDecimal finalPrice;
    private Set<OrderItemDTO> orderItems;
    private Set<OrderStatusHistoryDTO> orderStatusHistories;

    @FieldMetadata(title = "Ghi chú")
    private String note;

    @FieldMetadata(title = "Lý do hủy")
    private String cancelReason;
}
