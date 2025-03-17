package com.group3.sp25hsf302group3se1889vj.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDTO extends BaseDTO {
    private Long productId;
    private Long productVariantId;
    private ProductDTO product;
    private ProductVariantDTO productVariant;
    private int quantity;
}
