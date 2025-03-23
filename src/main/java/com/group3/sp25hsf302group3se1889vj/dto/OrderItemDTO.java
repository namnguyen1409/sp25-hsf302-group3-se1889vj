package com.group3.sp25hsf302group3se1889vj.dto;

import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO extends BaseDTO {
    private Long orderId;
    private Long productId;
    private ProductDTO product;
    private ProductVariantDTO productVariant;
    private Long productVariantId;
    private int quantity;
    private BigDecimal price;

}
