package com.group3.sp25hsf302group3se1889vj.dto.filter;

import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterDTO extends BaseFilterDTO {
    // TODO: Add fields here

    private String name;
    private String description;
    private String thumbnail;
    private String brandName;
    private String categoryName;
    private String priceOriginFrom;
    private String priceOriginTo;
    private String priceSaleFrom;
    private String priceSaleTo;

}
