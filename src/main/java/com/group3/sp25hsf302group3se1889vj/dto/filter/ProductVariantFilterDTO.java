package com.group3.sp25hsf302group3se1889vj.dto.filter;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantFilterDTO extends BaseFilterDTO {
    private Long productId;
    private String productSize;
    private String productColor;
    private Integer quantityFrom;
    private Integer quantityTo;
}
