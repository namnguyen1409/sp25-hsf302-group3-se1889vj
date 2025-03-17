package com.group3.sp25hsf302group3se1889vj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerProductDTO {
    private Long id;
    private boolean isActive;
    private String name;
    private String description;
    private String thumbnail;
    private BrandDTO brand;
    private CategoryDTO category;
    private BigDecimal priceOrigin;
    private BigDecimal priceSale;
    private int quantity;
    private Set<ProductImageDTO> productImages;
    private Set<ProductVariantDTO> productVariants;
}
